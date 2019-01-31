package com.neptune.player;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
/**
 *
 * @author Chester
 */
public class MyJavaTest {
public static void main(String args[])
    {
        try
        {            
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec("amixer get Master | awk '$0~/%/{print $4}' | tr -d '[]'");
            InputStream stdin = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(stdin);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            System.out.println("<OUTPUT>");
            while ( (line = br.readLine()) != null)
{                System.out.println(line);
Thread.sleep(1000);}
            System.out.println("</OUTPUT>");
            int exitVal = proc.waitFor();            
            System.out.println("Process exitValue: " + exitVal);
        } catch (Throwable t)
          {
            t.printStackTrace();
          }
    }
    /**public static void main(String[] args) throws IOException {
		Runtime rt = Runtime.getRuntime();
Process proc = rt.exec("awk -F\"[][]\" '/dB/ { print $2 }' <(amixer sget Master)");
            int exitVal = proc.exitValue();
            System.out.println("Process exitValue: " + exitVal);
       
try {
                    Runtime.getRuntime().exec("amixer sset Master 15%-,15%-");
                } catch (Exception ex) {
                    NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, null, ex);
                }
                Thread.sleep(500L);
                try {
                    Runtime.getRuntime().exec("amixer sset Master 15%-,15%-");
                } catch (Exception ex) {
                    NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, null, ex);
                }
                Thread.sleep(500L);
                try {
                    Runtime.getRuntime().exec("amixer sset Master 10%-,10%-");
                } catch (Exception ex) {
                    NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, null, ex);
                }
                Thread.sleep(500L);
                try {
                    Runtime.getRuntime().exec("amixer sset Master 10%-,10%-");
                } catch (Exception ex) {
                    NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, null, ex);
                }
                Thread.sleep(500L);
                try {
                    Runtime.getRuntime().exec("amixer sset Master 10%-,10%-");
                } catch (Exception ex) {
                    NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, null, ex);
                }
                Thread.sleep(500L);
                try {
                    Runtime.getRuntime().exec("amixer sset Master 10%-,10%-");
                } catch (Exception ex) {
                    NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, null, ex);
                }
                Thread.sleep(500L);
                try {
                    Runtime.getRuntime().exec("amixer sset Master 10%-,10%-");
                } catch (Exception ex) {
                    NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, null, ex);
                }
                NeptuneLogger.getInstance().getLogger().log(Level.INFO, "Volume fade out completed.");
            } catch (Exception ex) {
                NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, null, ex);
            } finally {
                try {
                    StreamPlayer.stop();
//                    Runtime.getRuntime().exec("amixer -D pulse sset Master 50,50");
//                    Runtime.getRuntime().exec("amixer -D pulse sset Master 50,50");
//                    Runtime.getRuntime().exec("amixer -D pulse sset Master 50,50");
                } catch (Exception ex) {
                    NeptuneLogger.getInstance().getLogger().log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        Runtime.getRuntime().exec("amixer sset Master " + volume+ "+,"+ volume + "+");
                        NeptuneLogger.getInstance().getLogger().log(Level.INFO, "Volume increased...");
                    } catch (IOException ex) {*/
    
}