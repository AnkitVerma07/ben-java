package careers.nebula.ben.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.google.i18n.phonenumbers.PhoneNumberMatch;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

import careers.nebula.ben.db.enitity.Keywords;
import careers.nebula.ben.db.repository.UserProfileRepo;
import careers.nebula.ben.information.DocumentInformation;

/**
 *
 * @author Ankit_Nebula
 */
public class ResumeParser {
    
private DocumentInformation docuInfo;
private CSVServicing key;
private UserProfileRepo userpro;
private String KEYWORDS_CSV = "/Users/pinky/Documents/Nebula_Documents/Files/keywords.csv";
 
 public boolean stringContainsItemFromList(String inputString, String[] items){
    for(int i =0; i < items.length; i++)
    {
        if(inputString.contains(items[i]))
        {
            return true;
        }
    }
    return false;
}

 public String userName(String fileName) throws IOException{
	 docuInfo = new DocumentInformation();
     String para =  docuInfo.FileType(fileName);
     String tokens[] = para.split("\\s+");
     String temp = tokens[0] + tokens[1];
     return temp;
 }
 
 public String userEmail(String fileName) throws IOException{
	 docuInfo = new DocumentInformation();
     String para =  docuInfo.FileType(fileName);
    String tempEmail = null;
        String[] split = para.split("\\s+");
        for(int j =0; j <= split.length-1; j++){
            Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
            Matcher matcher = pattern.matcher(split[j]);
                if(matcher.matches()){
                    tempEmail = split[j];
                    //store this splited[i] email in the DB
                    }else{
                         //ignore if it is not a match
                        }
                    }  
    return tempEmail;
 }
 
 //keep in mind the method is implemented with assumption the resume will hold only one any kind of phone number.
 public String userPhoneNumber(String fileName) throws IOException{
	 docuInfo = new DocumentInformation();
     String para = docuInfo.FileType(fileName);
     long tempPhone = 0;
     Iterator<PhoneNumberMatch> existsPhone=PhoneNumberUtil.getInstance().findNumbers(para, "IN").iterator();

     while (existsPhone.hasNext()){
    	 tempPhone =  existsPhone.next().number().getNationalNumber();
     }
     String strLong = Long.toString(tempPhone); 
    return strLong;
 }
 
 
 public String userAddress(String fileName) throws IOException{
	 docuInfo = new DocumentInformation();
     String para =  docuInfo.FileType(fileName);
    String tempstate = null;
        String[] split = para.split("\\s+");
        
        for(int j =0; j <= split.length-1; j++){
            Pattern pattern = Pattern.compile("AL|AK|AR|AZ|CA|CO|CT|DC|DE|FL|GA|HI|IA|ID|IL|IN|KS|KY|LA|MA|MD|ME|MI|MN|MO|MS|MT|NC|ND|NE|NH|NJ|NM|NV|NY|OH|OK|OR|PA|RI|SC|SD|TN|TX|UT|VA|VT|WA|WI|WV|WY");
            Matcher matcher = pattern.matcher(split[j]);
                if(matcher.matches()){
                	tempstate = split[j];
                    }else{
                         //ignore if it is not a match
                        }
                    }  
        String tempzip = null;
        for(int j =0; j <= split.length-1; j++){
        	Pattern pattern2 = Pattern.compile("^[0-9]{5}(?:-[0-9]{4})?$");
            Matcher matcher2 = pattern2.matcher(split[j]);
                if(matcher2.matches()){
                	tempzip = split[j];
                    }else{
                         //ignore if it is not a match
                        }
                    } 
        String tempstreet = null;
        for(int j =0; j <= split.length-1; j++){
        	Pattern pattern3 = Pattern.compile("AL|AK|AR|AZ|CA|CO|CT|DC|DE|FL|GA|HI|IA|ID|IL|IN|KS|KY|LA|MA|MD|ME|MI|MN|MO|MS|MT|NC|ND|NE|NH|NJ|NM|NV|NY|OH|OK|OR|PA|RI|SC|SD|TN|TX|UT|VA|VT|WA|WI|WV|WY");
            Matcher matcher3 = pattern3.matcher(split[j]);
                if(matcher3.matches()){
                	for(int x = 0; x < split.length; x++){
                		
                	}
                	tempstreet = split[j-2] + split[j-1];
                	//store previous until you hit an int value
                    }else{
                         //ignore if it is not a match
                        }
                    } 
   String tempAddress = tempstreet + tempstate + tempzip;
    return tempAddress;  
 }
 
 //this gives out http and https links so at times outputs duplicates if links are hyperlink in the document
 public String userLinks(String fileName) throws IOException{
	 docuInfo = new DocumentInformation();
     String para =  docuInfo.FileType(fileName);
     final Pattern urlPattern = Pattern.compile(
             "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
                     + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
                     + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
             Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
     Matcher matcher = urlPattern.matcher(para);
     List<Integer> startIndexList = new ArrayList<Integer>();
     List<Integer> endIndexList = new ArrayList<Integer>();
     List<String> links = new ArrayList<String>();
     while (matcher.find()) {
          startIndexList.add(matcher.start(1));
          endIndexList.add(matcher.end());
     }   
     for(int i =0; i < startIndexList.size()-1; i++){
    		 links.add(para.substring(startIndexList.get(i), endIndexList.get(i))); 
     }
     String temp =  "";
   for(int a = 0; a < links.size(); a++){
	 temp = temp + "\n" +links.get(a); 
   }
    return temp;
 }
 
 public Map<String, String> userParseData(String fileName) throws IOException{
	 docuInfo = new DocumentInformation();
     String para =  docuInfo.FileType(fileName);
     String tempExperience = "";
     Map<String, String> parsedata = new HashMap<String, String>();
     List<String> lookupList = new ArrayList<String>();
     List<Integer> foundList = new ArrayList<Integer>();
     lookupList.add("Experience");
     lookupList.add("Education");
     lookupList.add("Summary");
     lookupList.add("Skills");
     lookupList.add("Objective");
     lookupList.add("Expertise");
     lookupList.add("Competencies");
     lookupList.add("Certifications");
     lookupList.add("Professional Affiliations");
     lookupList.add("Courses");
     lookupList.add("Publications");
     lookupList.add("Research");
     String[] split = para.split("\\s+");
     for(int i =0; i <= split.length-1; i++){
    	 for(int j = 0; j < lookupList.size(); j++){
    		if(para.contains(lookupList.get(j))){
    			foundList.add(para.lastIndexOf(lookupList.get(j)));
    			Collections.sort(foundList);
    		}
    	 }
      }
     int size = foundList.size();
     for (int i = 0; i < size - 1; i++) {
         for (int j = i + 1; j < size; j++) {
             if (!foundList.get(j).equals(foundList.get(i)))
                 continue;
             foundList.remove(j);
             j--; 
             size--;
         } 
     }      
     for(int i=0; i<foundList.size()-1; i++){
    	 if(!foundList.get(i+1).equals(null)){
    		tempExperience = para.substring(foundList.get(i),foundList.get(i+1));
    		String temp[] = tempExperience.split(" ");
    		parsedata.put(temp[0], tempExperience);
    	 }  
     }
     String lastdata = para.substring(foundList.get(foundList.size()-1));
     String lasttemp[] = lastdata.split(" ");
     parsedata.put(lasttemp[0], lastdata);
     return parsedata;
 }
 
 public String userExperience(String fileName) throws IOException{
	 Map<String, String> parsedata = new HashMap<String, String>();
	 parsedata = userParseData(fileName);
	 String temp = parsedata.get("Experience");
	 return temp;
 }
 
 public String userEducation(String fileName) throws IOException{
	 Map<String, String> parsedata = new HashMap<String, String>();
	 parsedata = userParseData(fileName);
	 String temp = parsedata.get("Education");
	 return temp;
 }
 
 public String userAboutme(String fileName) throws IOException{
	 Map<String, String> parsedata = new HashMap<String, String>();
	 parsedata = userParseData(fileName);
	 String temp = parsedata.get("Summary");
	// System.out.println(temp);
	 return temp;
 }
 
 public String userGoals(String fileName) throws IOException{
	 Map<String, String> parsedata = new HashMap<String, String>();
	 parsedata = userParseData(fileName);
	 String temp = parsedata.get("Objective");
	// System.out.println(temp);
	 return temp;
 }
 
 public String userSkills(String fileName) throws IOException{
	 Map<String, String> parsedata = new HashMap<String, String>();
	 parsedata = userParseData(fileName);
	 String temp = parsedata.get("Skills") + parsedata.get("Professional Affiliations") + parsedata.get("Competencies") + parsedata.get("Expertise");
	// System.out.println(temp);
	 return temp;
 }
 
 public String userResearch(String fileName) throws IOException{
	 Map<String, String> parsedata = new HashMap<String, String>();
	 parsedata = userParseData(fileName);
	 String temp = parsedata.get("Research") + parsedata.get("Publications");
	// System.out.println(temp);
	 return temp;
 }
 
 public String userCourses(String fileName) throws IOException{
	 Map<String, String> parsedata = new HashMap<String, String>();
	 parsedata = userParseData(fileName);
	 String temp = parsedata.get("Courses");
	// System.out.println(temp);
	 return temp;
 }
 
 public String userKeywordsMatched(String fileName) throws IOException{
	 String search = null;
     List<String> foundUniName = new ArrayList<>();
     docuInfo = new DocumentInformation();
     String para =  docuInfo.FileType(fileName);
     userpro = new UserProfileRepo();
     List<Keywords> keywordsData = new ArrayList<Keywords>();
     keywordsData = userpro.getAllKeywordsData();
     key = new CSVServicing();
   String[] searchKeyword = key.csvReader(KEYWORDS_CSV); 
   for(int i =0; i<searchKeyword.length; i++){
   search = searchKeyword[i];
   int intIndex = para.indexOf(search);
      if(intIndex != - 1){
    	  foundUniName.add(search);
      }else{
    	   for(Keywords k : keywordsData){
    		   if(para.contains(k.getName())){
    			   foundUniName.add(k.getName());
    		   }
    	   }   	  
      }
   }
   String temp = " ";
     for(String s : foundUniName){
    	 temp = temp+s+",";
     }
	 return temp;
 }
 
  public String userKeywordsNotMatched(String fileName) throws IOException{
		 String search = null;
	     List<String> notfoundUniName = new ArrayList<>();
	     docuInfo = new DocumentInformation();
	     String para =  docuInfo.FileType(fileName);
	     userpro = new UserProfileRepo();
	     List<Keywords> keywordsData = new ArrayList<Keywords>();
	     keywordsData = userpro.getAllKeywordsData();
	     key = new CSVServicing();
	   String[] searchKeyword = key.csvReader(KEYWORDS_CSV);
	   for(int i =0; i<searchKeyword.length; i++){
	   search = searchKeyword[i];
	   int intIndex = para.indexOf(search);
	      if(intIndex == - 1){
	    	  notfoundUniName.add(search);
	      }else{
	    	   for(Keywords k : keywordsData){
	    		   if(para.contains(k.getName())){
	    			   
	    		   } else {
	    			   notfoundUniName.add(k.getName());
	    		   }
	    	   } 
	      }
	   }
	   String temp = " ";
	     for(String s : notfoundUniName){
	    	 temp = temp+s+",";
	     }
		 return temp;
 }
  
}
