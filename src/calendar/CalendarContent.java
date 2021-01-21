package calendar;

import java.util.Calendar;

public class CalendarContent {
	
	Calendar cal = Calendar.getInstance();
	int day[][][] = new int[12][6][7];  //[��][��][����]
    String week[] = new String[] {
        "��","��","ȭ","��","��","��","��"
    };
    

    
    public CalendarContent() {
    	int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
    	System.out.println(dayofweek);
    	for(int month=0; month<12; month++) {
    		cal.set(Calendar.MONTH, month);
    		int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    		
    		for(int i=1; i<=maxDay; i++) {
    			cal.set(Calendar.DATE, i);
    			day[cal.get(Calendar.MONTH)]
    					[cal.get(Calendar.WEEK_OF_MONTH)-1]
    							[cal.get(Calendar.DAY_OF_WEEK)-1] = i;
    		}
    	}
    	
    	for(int mon = 0; mon<12; mon++) {
    		int mm = mon+1;
//    		System.out.print(mm+"��");
    		
    		for(int i=0; i<week.length; i++) {
//    			System.out.print(week[i]);
    		}
//    		System.out.println();
    		
    		for(int i=0; i<6; i++) {
    			for(int j=0; j<7; j++) {
    				if(day[mon][i][j] != 0) {
//    					System.out.print(day[mon][i][j]+"\t");
    				}else {
//    					System.out.print("\t");
    				}
    			}
//    			System.out.println();
    		}
    	}
	}




}
