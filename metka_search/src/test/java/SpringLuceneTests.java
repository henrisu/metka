import fi.uta.fsd.metka.data.entity.RevisionEntity;
import fi.uta.fsd.metka.data.entity.key.RevisionKey;
import fi.uta.fsd.metkaSearch.IndexerComponent;
import fi.uta.fsd.metkaSearch.LuceneConfig;
import fi.uta.fsd.metkaSearch.analyzer.FinnishVoikkoAnalyzer;
import fi.uta.fsd.metkaSearch.directory.DirectoryInformation;
import fi.uta.fsd.metkaSearch.directory.DirectoryManager;
import fi.uta.fsd.metkaSearch.commands.indexer.DummyIndexerCommand;
import fi.uta.fsd.metkaSearch.commands.indexer.WikipediaIndexerCommand;
import fi.uta.fsd.metkaSearch.enums.IndexerConfigurationType;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.search.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext.xml")
public class SpringLuceneTests {
    @Autowired
    private IndexerComponent indexer;

    @PersistenceContext(name = "entityManager")
    private EntityManager em;

    @Test
    public void indexerRunningTest() throws IOException {
        try {
            int loops = 0;
            DirectoryManager.DirectoryPath path = DirectoryManager.formPath(true, IndexerConfigurationType.DUMMY, null);
            indexer.startIndexer(path);
            while(indexer.hasRunningIndexers()) {
                Thread.sleep(15000);
                loops++;
                System.err.println("Test loops: "+loops);
                indexer.addCommand(DummyIndexerCommand.index(path.isUseRam(), path.getLanguage()));
                if(loops == 5) {
                    indexer.stopIndexer(path);
                }
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void wikipediaIndexingTest() {
        try {
            DirectoryManager.DirectoryPath path = DirectoryManager.formPath(false, IndexerConfigurationType.WIKIPEDIA, "en");
            indexer.startIndexer(path);
            if(indexer.hasRunningIndexers()) {
                indexer.addCommand(WikipediaIndexerCommand.index(path, "/home/lasseku/wikipedia/en/enwiki-latest-pages-articles1.xml-p000000010p000010000"));
                indexer.addCommand(WikipediaIndexerCommand.index(path, "/home/lasseku/wikipedia/en/enwiki-latest-pages-articles2.xml-p000010002p000024999"));
                indexer.addCommand(WikipediaIndexerCommand.index(path, "/home/lasseku/wikipedia/en/enwiki-latest-pages-articles3.xml-p000025001p000055000"));
                indexer.addCommand(WikipediaIndexerCommand.index(path, "/home/lasseku/wikipedia/en/enwiki-latest-pages-articles4.xml-p000055002p000104998"));
                indexer.addCommand(WikipediaIndexerCommand.index(path, "/home/lasseku/wikipedia/en/enwiki-latest-pages-articles5.xml-p000105002p000184999"));
                indexer.addCommand(WikipediaIndexerCommand.index(path, "/home/lasseku/wikipedia/en/enwiki-latest-pages-articles6.xml-p000185003p000305000"));
                indexer.addCommand(WikipediaIndexerCommand.stop(path));
            }
            while(indexer.hasRunningIndexers()) {
                Thread.sleep(1000);
            }
            System.err.println("Indexing complete");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void wikipediaIndexingSearchTest() {
        try {
            DirectoryManager.DirectoryPath path = DirectoryManager.formPath(false, IndexerConfigurationType.WIKIPEDIA, "en");
            DirectoryInformation indexer = DirectoryManager.getIndexDirectory(path);
            IndexReader reader = indexer.getIndexReader();
            IndexSearcher searcher = new IndexSearcher(reader);
            Query query;
            BooleanQuery bQuery;
            //query = new TermQuery(new Term("pageId", "6944"));
            bQuery = new BooleanQuery();
            query = new WildcardQuery(new Term("pageId", "6???"));
            bQuery.add(query, BooleanClause.Occur.MUST);
            query = new WildcardQuery(new Term("title", "C*"));
            bQuery.add(query, BooleanClause.Occur.MUST);
            query = new WildcardQuery(new Term("pageId", "???7"));
            bQuery.add(query, BooleanClause.Occur.MUST_NOT);
            printSearchResult(searcher, bQuery);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void fiWikipediaIndexingTest() {
        try {
            DirectoryManager.DirectoryPath path = DirectoryManager.formPath(false, IndexerConfigurationType.WIKIPEDIA, "fi");
            indexer.startIndexer(path);
            if(indexer.hasRunningIndexers()) {
                indexer.addCommand(WikipediaIndexerCommand.index(path, "/home/lasseku/wikipedia/fi/fiwiki-latest-pages-articles.xml"));
                indexer.addCommand(WikipediaIndexerCommand.stop(path));
            }
            while(indexer.hasRunningIndexers()) {
                Thread.sleep(1000);
            }
            System.err.println("Indexing complete");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void fiWikipediaIndexSearchTest() {
        try {
            DirectoryManager.DirectoryPath path = DirectoryManager.formPath(false, IndexerConfigurationType.WIKIPEDIA, "fi");
            DirectoryInformation indexer = DirectoryManager.getIndexDirectory(path);
            IndexReader reader = indexer.getIndexReader();
            IndexSearcher searcher = new IndexSearcher(reader);
            Query query;
            BooleanQuery bQuery;
            bQuery = new BooleanQuery();
            Map<String, Analyzer> analyzers = new HashMap<>();
            FinnishVoikkoAnalyzer voikkoAnalyzer = new FinnishVoikkoAnalyzer();
            for(int i = 1; i <= 1; i++) {
                for(int j = 1; j <= 1; j++) {
                    analyzers.put("topic"+i+".word"+j, voikkoAnalyzer);
                }
            }
            PerFieldAnalyzerWrapper analyzer = new PerFieldAnalyzerWrapper(new WhitespaceAnalyzer(LuceneConfig.USED_VERSION), analyzers);
            StandardQueryParser parser = new StandardQueryParser(analyzer);
            parser.setAllowLeadingWildcard(true);
            //bQuery.add(parser.parse("title:*", "pageId"), BooleanClause.Occur.MUST);
            for(int i = 1; i <= 5; i++) {
                BooleanQuery subB = new BooleanQuery();
                for(int j = 1; j <= 30; j++) {
                    subB.add(parser.parse("topic"+i+".word"+j+":kansa", "pageId"), BooleanClause.Occur.SHOULD);
                }
                bQuery.add(subB, BooleanClause.Occur.MUST);
            }
            /*bQuery.add(parser.parse("topic1:eu*", "pageId"), BooleanClause.Occur.MUST);
            bQuery.add(parser.parse("topic2:eu*", "pageId"), BooleanClause.Occur.MUST);
            bQuery.add(parser.parse("topic3:eu*", "pageId"), BooleanClause.Occur.MUST);
            bQuery.add(parser.parse("topic4:eu*", "pageId"), BooleanClause.Occur.MUST);
            bQuery.add(parser.parse("topic5:eu*", "pageId"), BooleanClause.Occur.MUST);
            bQuery.add(parser.parse("topic6:eu*", "pageId"), BooleanClause.Occur.MUST);
            bQuery.add(parser.parse("topic7:eu*", "pageId"), BooleanClause.Occur.MUST);
            bQuery.add(parser.parse("topic8:eu*", "pageId"), BooleanClause.Occur.MUST);
            bQuery.add(parser.parse("topic9:eu*", "pageId"), BooleanClause.Occur.MUST);
            bQuery.add(parser.parse("topic10:eu*", "pageId"), BooleanClause.Occur.MUST);
            bQuery.add(parser.parse("topic11:eu*", "pageId"), BooleanClause.Occur.MUST);
            bQuery.add(parser.parse("topic12:eu*", "pageId"), BooleanClause.Occur.MUST);
            bQuery.add(parser.parse("topic13:eu*", "pageId"), BooleanClause.Occur.MUST);
            bQuery.add(parser.parse("topic14:eu*", "pageId"), BooleanClause.Occur.MUST);
            bQuery.add(parser.parse("topic15:eu*", "pageId"), BooleanClause.Occur.MUST);
            bQuery.add(parser.parse("topic16:e*", "pageId"), BooleanClause.Occur.MUST);
            bQuery.add(parser.parse("topic17:e*", "pageId"), BooleanClause.Occur.MUST);
            bQuery.add(parser.parse("topic18:e*", "pageId"), BooleanClause.Occur.MUST);
            bQuery.add(parser.parse("topic19:e*", "pageId"), BooleanClause.Occur.MUST);
            bQuery.add(parser.parse("topic20:e*", "pageId"), BooleanClause.Occur.MUST);
            bQuery.add(parser.parse("topic21:e*", "pageId"), BooleanClause.Occur.MUST);
            bQuery.add(parser.parse("topic22:e*", "pageId"), BooleanClause.Occur.MUST);
            bQuery.add(parser.parse("topic23:e*", "pageId"), BooleanClause.Occur.MUST);
            bQuery.add(parser.parse("topic24:e*", "pageId"), BooleanClause.Occur.MUST);
            bQuery.add(parser.parse("topic25:e*", "pageId"), BooleanClause.Occur.MUST);*/
            /*query = new WildcardQuery(new Term("pageId", "6???"));
            bQuery.add(query, BooleanClause.Occur.MUST);
            query = new WildcardQuery(new Term("title", "C*"));
            bQuery.add(query, BooleanClause.Occur.MUST);
            query = new WildcardQuery(new Term("pageId", "???7"));
            bQuery.add(query, BooleanClause.Occur.MUST_NOT);*/
            printSearchResult(searcher, bQuery);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void emTest() {
        RevisionEntity revision = em.find(RevisionEntity.class, new RevisionKey(1L, 1));
        assertNotNull(revision);
    }

    private void printSearchResult(IndexSearcher searcher, Query qr) throws IOException {
        long start = System.currentTimeMillis();
        TopDocs hits = searcher.search(qr, 100);
        System.err.println("Query performed in "+(System.currentTimeMillis()-start)+"ms");

        System.err.println("Hits: " + hits.totalHits);
        System.err.println("PageId (Title)");
        for(ScoreDoc doc : hits.scoreDocs) {
            Document document = searcher.doc(doc.doc);
            /*for(IndexableField field : document.getFields()) {
                System.err.println("["+field.name()+"] "+field.stringValue());
            }*/
            /*for(int i = 1; i <= 4; i++) {
                for(int j = 1; j <= 10; j++) {
                    System.err.println("["+i+","+j+"] "+document..get("topic"+i+".word"+j));
                }
            }*/
            System.err.println(document.get("pageId")+"("+document.get("title")+")");
        }

    }
}