package net.mua.jypsum;

public class Main {
	    @SuppressWarnings("unused")
		public Main () {
	        UI gui = new UI();
	    }
	    @SuppressWarnings("unused")
		public static void main (String[] args) {
	        for(int i = 0; i < args.length; i++) {
	            //Do options
	        }
//	        Main myJypsum = new Main();
            Msg debugMsg = new Msg("bob@aol.com", 
                    "rob@aol.com", 
                    "john@aol.com", 
                    "Stuff", 
                    "buddda buddaa", 
                    true, 
                    "smtp.google.com");
            System.out.println("");
	    }
	}

