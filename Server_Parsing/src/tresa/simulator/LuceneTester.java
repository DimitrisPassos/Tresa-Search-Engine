package tresa.simulator;

import java.io.File;
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
        Server server = new Server();
        server.start();
        LuceneTester tester = new LuceneTester();
        Scanner scanner = new Scanner(System.in);

        while (true){
            int selection;
            System.out.println("Enter Choice");
            selection = scanner.nextInt();
            if (selection == 1){ // Adds Reuters TODO REMOVE IN THE END
                try {
                    //tester = new LuceneTester();
                    tester.createIndex();

                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }
            }else if(selection == 2){ // Add folder
                System.out.println("Name of dir");
                String selectedDir = scanner.next();
                try {
                    tester.createOneIndex(selectedDir);
                    tester.search("zone");

                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }

            }else if(selection == 3){ // Add Single File
                System.out.println("Name of file");
                String selectedFile = server.complete;
                try {
                    tester.singleFile(selectedFile);
                }catch (IOException | ParseException e){
                    e.printStackTrace();
                }
                server.complete = "";

            }else if (selection == 4){ // Delete file
                System.out.println("Name of the file for deletion");
                String fileToDelete = scanner.next();
                try {
                    //tester.fileToDelete(fileToDelete);
                    tester.testFileToDelete(fileToDelete);
                    tester.search("zone");
                }catch (IOException | ParseException e){
                    e.printStackTrace();
                }
            }

        }


    }
    private void createIndex() throws IOException, ParseException {
        sec = new SecondIndex(indexDir);
        int numIndexed;
        long startTime = System.currentTimeMillis();
        numIndexed = sec.createIndex(dataDir, new TextFileFilter());
        long endTime = System.currentTimeMillis();
        sec.close();
        System.out.println(numIndexed+" File(s) indexed, time taken: " +
                (endTime-startTime)+" ms");
    }

    //TODO MERGE createIndex && createOneIndex AT THE END

    protected void createOneIndex(String selectedDir) throws IOException, ParseException {
        sec = new SecondIndex(indexDir);
        int numIndexed;
        long startTime = System.currentTimeMillis();
        numIndexed = sec.createIndex(selectedDir, new TextFileFilter());
        long endTime = System.currentTimeMillis();
        sec.close();
        System.out.println(numIndexed+" File(s) indexed, time taken: " +
                (endTime-startTime)+" ms");
    }

    protected void singleFile(String selectedFile) throws IOException, ParseException {
        sec = new SecondIndex(indexDir);
        int numIndexed;
        long startTime = System.currentTimeMillis();
        numIndexed = sec.createSingleIndex(selectedFile, new TextFileFilter());
        long endTime = System.currentTimeMillis();
        sec.close();
        System.out.println(numIndexed+" File(s) indexed, time taken: " +
                (endTime-startTime)+" ms");
    }

    private void testFileToDelete(String deleteFile) throws IOException {
        File file = new File(deleteFile);
        sec = new SecondIndex(indexDir);
        sec.deletingFiles(deleteFile);
        sec.close();
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

