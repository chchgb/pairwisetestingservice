package st;
 import org.antlr.stringtemplate.*;
 import org.antlr.stringtemplate.language.*;
 import java.lang.reflect.*;
import java.io.*;

 public class Dump {

    // some dummy fields
    public int i;
    public String name;
    public int[] data;

    public static void main(String[] args) throws Exception {
		StringTemplateGroup templates = new StringTemplateGroup(new FileReader("templates/Java.stg"), AngleBracketTemplateLexer.class);
		// StringTemplateGroup templates = new StringTemplateGroup(new FileReader("templates/XML.stg"), DefaultTemplateLexer.class);

		Class<?> c = Dump.class;
        Field[] fields = c.getFields();
        Method[] methods = c.getDeclaredMethods();
        StringTemplate classST = templates.getInstanceOf("class");
        classST.setAttribute("name", c.getName());
        classST.setAttribute("fields", fields);
        classST.setAttribute("methods", methods);
        System.out.println(classST);
    }

    // dummy methods
    public void foo(int x, float[] y) {;}
    public String bar() {return "";}
 }