package com.dk.trender;

/**
 * 
 * @author ayrton
 * @date 2017-03-31 01:13:42
 */
public class MigrateApplication {
    public static void main(String[] args) throws Exception {
    	new TrenderApplication().run("db", "migrate", "trender.yml");
	}
}
