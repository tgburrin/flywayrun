package net.tgburrin.flywayrun;

import java.util.Hashtable;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class GetOpts {
    public static Hashtable<String,String> parseOpts (String optstr, String[] args) {
        Hashtable<String, String> Options = new Hashtable<String, String>();

        int argn = 0;
        int optn = 0;
        Boolean OptArg = false;
        String PrevOptArg = null;

        // parse command line
        String[] OptList = optstr.split("\\s+");
        Pattern p = Pattern.compile("^-.*");
        for(argn = 0; argn < args.length; argn++){
            Matcher m = p.matcher(args[argn]);
            if(m.matches() && OptArg == true){
                System.err.println("Option "+PrevOptArg+" requires an argument");
                System.exit(1);
            } else if (m.matches()){
                args[argn] = args[argn].replaceFirst("-","");
                for(optn = 0; optn < OptList.length; optn++){
                    if(OptList[optn].compareTo(args[argn])==0){
                        Options.put("opt_"+OptList[optn],new String());
                    } else if (OptList[optn].compareTo(args[argn]+":")==0){
                        PrevOptArg = OptList[optn].substring(0,OptList[optn].indexOf(":"));
                        OptArg = true;
                    }
                }
            } else if (OptArg == true){
                Options.put("opt_"+PrevOptArg,new String(args[argn]));
                OptArg = false;
            } else {
                System.err.println("Unhandled option '"+args[argn]+"'");
            }
        }
        return Options;
    }
}