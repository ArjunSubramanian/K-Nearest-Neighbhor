/**
 To find k nearest neighbors of a new instance
 Please watch my explanation of how KNN works: xxx
 
 - For classification it uses majority vote
 - For regression it finds the mean (average)
*/  

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

class KNN
{

   
  
 /**
 * Returns the majority value in an array of strings
 * majority value is the most frequent value (the mode)
 * handles multiple majority values (ties broken at random)
 *
 * @param  array an array of strings
 * @return  the most frequent string in the array
 */ 
  
  static ArrayList<Application> Applications = new ArrayList<Application>();
  static ArrayList<Application> TestApplications = new ArrayList<Application>();
  static ArrayList<Application> ResultsApplications = new ArrayList<Application>();
private static String findMajorityClass(String[] array)
{
//add the String array to a HashSet to get unique String values
    Set<String> h = new HashSet<String>(Arrays.asList(array));
    //convert the HashSet back to array
    String[] uniqueValues = h.toArray(new String[0]);
    //counts for unique strings
    int[] counts = new int[uniqueValues.length];
    // loop thru unique strings and count how many times they appear in origianl array   
    for (int i = 0; i < uniqueValues.length; i++) {
        for (int j = 0; j < array.length; j++) {
            if(array[j].equals(uniqueValues[i])){
               counts[i]++;
            }
        }        
    }
               
   
    
    
    int max = counts[0];
    for (int counter = 1; counter < counts.length; counter++) {
        if (counts[counter] > max) {
            max = counts[counter];
        }
    }
    System.out.println("max # of occurences: "+max);
    
    // how many times max appears
    //we know that max will appear at least once in counts
    //so the value of freq will be 1 at minimum after this loop
    int freq = 0;
    for (int counter = 0; counter < counts.length; counter++) {
        if (counts[counter] == max) {
            freq++;
        }
    }
    
    //index of most freq value if we have only one mode
    int index = -1;
    if(freq==1){
       for (int counter = 0; counter < counts.length; counter++) {
         if (counts[counter] == max) {
            index = counter;
            break;
         }
       }
       //System.out.println("one majority class, index is: "+index);
       return uniqueValues[index];
    } else{//we have multiple modes
       int[] ix = new int[freq];//array of indices of modes
       System.out.println("multiple majority classes: "+freq+" classes");
       int ixi = 0;
       for (int counter = 0; counter < counts.length; counter++) {
         if (counts[counter] == max) {
            ix[ixi] = counter;//save index of each max count value
            ixi++; // increase index of ix array
         }
       }
       
       for (int counter = 0; counter < ix.length; counter++)         
            System.out.println("class index: "+ix[counter]);       
       
       //now choose one at random
       Random generator = new Random();        
       //get random number 0 <= rIndex < size of ix
       int rIndex = generator.nextInt(ix.length);
       System.out.println("random index: "+rIndex);
       int nIndex = ix[rIndex];
       //return unique value at that index 
       return uniqueValues[nIndex];
    }
       
}


/**
 * Returns the mean (average) of values in an array of doubless
 * sums elements and then divides the sum by num of elements
 *
 * @param  array an array of doubles
 * @return  the mean
 */ 
 private static double meanOfArray(double[] m) {
   double sum = 0.0;
    for (int j = 0; j < m.length; j++){
        sum += m[j];
    }
    return sum/m.length;
 }
	
  
 
  public static void main(String args[]){ 
	  long startTime = System.currentTimeMillis();
	  String csvFile = "D:/Course Work/Computer Security/TrainingSet1.csv";
		String TestFile="D:/Course Work/Computer Security/TestSet1.csv";
	  BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
	 
		try {
	 
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
	 
			        // use comma as separator
				String[] Application = line.split(cvsSplitBy);
				String Application_name=Application[0];
				String Label = Application[1];
				double[] perms = new double[Application.length-2];
				//System.out.println( Label);
				int j=0;
				for(int i=2;i<=Application.length-2;i++)
				{
				 perms[j]=Double.parseDouble(Application[i]);
				
				 j++;
				
				}
				Application app = new Application();
				app.setAplication_NAme(Application_name);
				app.setLabel(Label);
				app.setPermissions(perms);
				Applications.add(app);
			}
	 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			 
			br = new BufferedReader(new FileReader(TestFile));
			while ((line = br.readLine()) != null) {
	 
			        // use comma as separator
				String[] Application = line.split(cvsSplitBy);
				String Application_name=Application[0];
				//String Label = Application[1];
				double[] perms = new double[Application.length-1];
				//System.out.println( Label);
				int j=0;
				for(int i=1;i<=Application.length-1;i++)
				{
				 perms[j]=Double.parseDouble(Application[i]);
				
				 j++;
				
				}
				Application app = new Application();
				app.setAplication_NAme(Application_name);
				//app.setLabel(Label);
				app.setPermissions(perms);
				TestApplications.add(app);
			}
	 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	 
		System.out.println("Done");
	  
        int k = 10;// # of neighbours  
       
        //list to save distance result
       
        // add city data to cityList       
       int i=0;
     for(Application Test:TestApplications )
     {
    	 List<Result> resultList = new ArrayList<Result>();
    	 double[] query = Test.getPermissions();
    	//find disnaces
    	for(Application city : Applications){
    	   double dist = 0.0; 
    	   double[] perms = city.getPermissions();
    	
    	  i++;
    	   for(int j = 0; j <query.length; j++){    	     
    	     dist += Math.pow(perms[j] - query[j], 2) ;
    	     //System.out.print(city.cityAttributes[j]+" ");    	     
    	   }
    	 double distance = Math.sqrt( dist );
    	 resultList.add(new Result(distance,city.getLabel()));
    	
    	// System.out.println(distance);
    	} 
     
    	//System.out.println(resultList);
    	Collections.sort(resultList, new DistanceComparator());
    	String[] ss = new String[k];
    	for(int x = 0; x < k; x++){
    	    //System.out.println(Applications.get(x).Label+ " .... " + resultList.get(x).distance);
    	    //get classes of k nearest instances (city names) from the list into an array
    	    ss[x] = resultList.get(x).Label;
    	}
    	String majClass = findMajorityClass(ss);
        System.out.println("Class of new instance is: "+majClass + "Application" + Test.Aplication_NAme);   
      Application result = new Application();
      result.setAplication_NAme(Test.getAplication_NAme());
      System.out.println(result.getAplication_NAme());
      result.setLabel(majClass);
       ResultsApplications.add(result); 	
     }
     generateCsvFile();
     long endTime   = System.currentTimeMillis();
     long totalTime = endTime - startTime;
     System.out.println(totalTime);
  }//end main  
  
  private static void generateCsvFile()
  {
   try
   {
       FileWriter writer = new FileWriter("D:/Course Work/Computer Security/Resultset14/Result1.csv",false);
       writer.append("Application ");
       writer.append(',');
      
       writer.append("Label");
       writer.append('\n');
 for(int i=0;i<ResultsApplications.size();i++){
          
       writer.append(ResultsApplications.get(i).getAplication_NAme());
       
                   writer.append(',');
       writer.append(ResultsApplications.get(i).getLabel());
          
       writer.append('\n');
 }  
            
       //generate whatever data you want
 
       writer.flush();
       writer.close();
   }
   catch(IOException e)
   {
        e.printStackTrace();
   }
   }

  //simple comparator class used to compare results via distances
  static class DistanceComparator implements Comparator<Result> {
     @Override
     public int compare(Result a, Result b) {
        return a.distance < b.distance ? -1 : a.distance == b.distance ? 0 : 1;
     }
  }
	       
}

























