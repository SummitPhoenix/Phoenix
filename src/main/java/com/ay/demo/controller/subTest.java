package com.ay.demo.controller;

public class subTest {
	public static int lengthOfLongestSubstring(String s) {
		if(s.equals("")){
            return 0;
        }
       int max = 1;
       
       if(s.length()==2) {
    	   if(s.charAt(0)==s.charAt(1)) {
    		   return 1;
    	   }else {
    		   return 2;
    	   }
       }
	    for(int i=1;i<s.length();i++){
	        for(int j=0;j<s.length()-i+1;j++){
	            String sub = s.substring(j,j+i);
	            int num=0;
	            for(int k=0;k<sub.length();k++) {
	            	char letter = sub.charAt(k);
	            	for(int l=0;l<sub.length();l++) {
	            		if(letter==sub.charAt(l)) {
	            			num++;
	            		}
	            	}
	            }
	            if(num==sub.length()) {
	            	if(i>=max) {
	            		max=i;
	            	}
	            }
	        }
	    }
	    return max;
	}
	public static void main(String[] args) {
		System.out.println(lengthOfLongestSubstring("abb"));
		for(int i=0;i<3;i++) {
			for(int j=0;j<3;j++) {
				System.out.println(i+" "+j);
			}
		}
	}
}
