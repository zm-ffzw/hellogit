package com.sky.chains;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.splitter.DocumentByParagraphSplitter;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Configuration
public class FaqConfig {

    //加载模型
    @Autowired
    private EmbeddingModel qwenEmbeddingModel;

    //创建向量存储器
    @Autowired
    private EmbeddingStore<TextSegment> qwenEmbeddingStore;

    @Bean
    public EmbeddingStore<TextSegment> qwenEmbeddingStore() {
        // 使用内存存储作为向量数据库
        return new InMemoryEmbeddingStore<>();
    }
    @Bean
    public ContentRetriever qwenContentRetriever() throws IOException {
        // 1.加载文档内容
        //Document document = FileSystemDocumentLoader.loadDocument("src/main/resources/faq.txt");
        Resource resource = new ClassPathResource("documents/faq.txt");
        Document document = FileSystemDocumentLoader.loadDocument(resource.getFile().toPath());

        // 2.文档切割(最多检索30，重叠字符10)
        DocumentByParagraphSplitter documentByParagraphSplitter = new DocumentByParagraphSplitter(30, 10);
        // 3.自定义文档加载器，将文档转换成向量并且存储子啊向量数据库中
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(documentByParagraphSplitter)
                .embeddingModel(qwenEmbeddingModel)
                .embeddingStore(qwenEmbeddingStore)
                .build();
        // 4.加载文档
        ingestor.ingest(document);
        // 5.创建内容检索器
        EmbeddingStoreContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingModel(qwenEmbeddingModel)
                .embeddingStore(qwenEmbeddingStore)
                .maxResults(5) // 最多返回5条数据
                .minScore(0.7) // 过滤小于0.7的分数
                .build();
        return contentRetriever;

    }
}
