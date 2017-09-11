package cs181;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * Word Count Mapper 
 * Receives lines of text, splits each line into words, and generates key, value pairs. Where 
 * the key is the word, and the value is just 1. The counts for a given key will be aggregated in the reducer. 
 *
 * @param  Raw text
 * @return < Key , 1 >
 * 
 */

public class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
	    private final IntWritable one = new IntWritable(1);
	    private Text word = new Text();
	    private String pattern= "^[a-z][a-z0-9]*$";
	    Set<String> stopWords = new HashSet<String>();
	    
	  
	    
	    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
	        
	    	  Scanner stopWordsTxt = new Scanner(new File("stop-word-list.txt"));
	  	    while (stopWordsTxt.hasNextLine()){
	  	    	 stopWords.add(stopWordsTxt.nextLine());
	  	    }
	    	
	    	String line = value.toString();  /* get line of text from variable 'value' and convert to string */
	    	
	    	/* Lets use a string tokenizer to split line by words using a pattern matcher */
	        StringTokenizer tokenizer = new StringTokenizer(line); 
	        
	        while (tokenizer.hasMoreTokens()) {
	            word.set(tokenizer.nextToken());
	            String stringWord = word.toString().toLowerCase().replaceAll("[^a-zA-Z ]","");
	             //Check for Stop Words
	            /* for each word, output the word as the key, and value as 1 */
	            if (stringWord.matches(pattern) && !stopWords.contains(stringWord)){
	                context.write(new Text(stringWord), one);
	            }
	            
	        }
	    }
	}