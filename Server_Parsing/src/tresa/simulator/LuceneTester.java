package tresa.simulator;

import java.io.IOException;
import java.util.Scanner;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

// Added 8.0.0

public class LuceneTester {
    String indexDir = "Index"; // REDO
    String dataDir = "Reuters";
    Indexer indexer;
    Searcher searcher;
    CustomIndex cIndex;
    SecondIndex sec;

    public static void main(String[] args) {
        LuceneTester tester;
        Scanner scanner = new Scanner(System.in);

        while (true){
            int selection;
            System.out.println("Enter Choice");
            selection = scanner.nextInt();
            if (selection == 1){
                try {
                    tester = new LuceneTester();
                    tester.createIndex();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if(selection == 2){

            }

        }




    }
    private void createIndex() throws IOException {
        sec = new SecondIndex(indexDir);
        int numIndexed;
        long startTime = System.currentTimeMillis();
        numIndexed = sec.createIndex(dataDir, new TextFileFilter());
        long endTime = System.currentTimeMillis();
        sec.close();
        System.out.println(numIndexed+" File(s) indexed, time taken: " +
                (endTime-startTime)+" ms");
    }

    private void search(String searchQuery) throws IOException,
            ParseException {
        searcher = new Searcher(indexDir);
        long startTime = System.currentTimeMillis();
        TopDocs hits = searcher.search(searchQuery);
        long endTime = System.currentTimeMillis();


        System.out.println(hits.totalHits +" documents found. Time :" +
                (endTime - startTime));
        for(ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = searcher.getDocument(scoreDoc);
            System.out.println("File: " +
                    doc.get(LuceneConstants.FILE_PATH));
        }
        searcher.close();
    }
}