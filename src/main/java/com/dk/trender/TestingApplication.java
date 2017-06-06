package com.dk.trender;

/**
 * 
 * @author ayrton
 * @date 2017-03-31 01:13:42
 */
public class TestingApplication {
    public static void main(String[] args) throws Exception {
    	new TrenderApplication().run("server", "src/main/resources/trender-test.yml");
	}
}
