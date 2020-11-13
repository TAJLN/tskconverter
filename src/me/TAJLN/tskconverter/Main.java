package me.TAJLN.tskconverter;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static File file;
    private static HashMap<String, String> replace = new HashMap<>();
    private static HashMap<String, String> pohabljenke = new HashMap<>();
    private static boolean istsk = false;
    private static JFrame f;

    //Messages
    private static String title = ".tsk Converter";
    private static String choose = "Choose file:";

    private static String convmodewaiting = "Conversion mode: Waiting...";
    private static String convmodenormal = "Conversion mode: To normal code";
    private static String convmodetsk = "Conversion mode: To .tsk";

    private static String backtoog = "This will convert .tsk file back to its original form.";
    private static String totsk = "This will convert your code .tsk in Slovenian language.";

    private static String fileerror = "There was an issue with getting the file";

    private static String convsuccess = "Conversion successful";
    private static String convertedtotsk = "Your code has been converted to .tsk for easy Slovenian use";
    private static String convbacktocode = "Your code has been converted from .tsk to the original language. Please note that this process is not 100% working and some syntax/caps errors may be present";

    public static void main(String[] args) {
        DoList();
        doPohabljenke();
        StartProgram();
    }
    private static void StartProgram()
    {
        JFrame guiFrame = new JFrame();
        handleframe(guiFrame);

        handleselection(guiFrame);
        handleconversion(guiFrame);

        guiFrame.setVisible(true);
    }

    private static void doPohabljenke(){
        pohabljenke.put("else if","kaj če");
        pohabljenke.put("private","zasebna");
        pohabljenke.put("boolean", "dvoumnost");
        pohabljenke.put("nullptr","ničelni_kazalec");
    }

    private static void DoList(){
        replace.put("if", "če");
        replace.put("else","drugače");
        replace.put("for", "za");

        replace.put("public","javna");
        replace.put("static", "statična");
        replace.put("new", "nova");

        replace.put("void","praznina");


        replace.put("string","vrvica");
        replace.put("int","številka");
        replace.put("char","znak");
        replace.put("float", "plavaj");
        replace.put("long", "dolga");
        replace.put("short", "kratka");

        replace.put("switch","stikalo");
        replace.put("default","privzeto");
        replace.put("case","primer");
        replace.put("break","prelomi");
        replace.put("continue","nadaljuj");

        replace.put("try","poizkusi");
        replace.put("catch","ujemi");


        replace.put("file","datoteka");
        replace.put("bool", "dvoum");
        replace.put("using", "uporabljaj");
        replace.put("return", "vrni");
        replace.put("rand", "naključ");
        replace.put("namespace","imenski_prostor");
        replace.put("NULL","ničelnost");
        replace.put("while","ko");

        replace.put("struct","struktura");
        replace.put("const", "konstanta");
        replace.put("include", "vsebuj");

        replace.put("hash", "Lojtrski");


        replace.put("true", "resnica");
        replace.put("false", "laž");

        replace.put("goto", "pojdi_na");
        replace.put("this", "to");
        replace.put("virtual", "navidezna");
        replace.put("register", "prijavi");

        replace.put("delete", "izbriši");

        replace.put("friend", "prijatelj");
        replace.put("Exception", "izjema");
        replace.put("map", "registrator");

        replace.put("and","in");
        replace.put("auto","samodejno");
        replace.put("bitand","bitno_in");
        replace.put("bitor","bitno_ali");
        replace.put("char8_t","znak8_t");
        replace.put("char16_t","znak_16t");
        replace.put("char32_t","znak32_t");
        replace.put("class","razred");
        replace.put("concept","koncept");
        replace.put("constinit","konstanten_začetek");
        replace.put("const_cast","konstanten_vlitek");
        replace.put("enum","naštevek");
        replace.put("do","naredi");
        replace.put("double","dvojna");
        replace.put("dynamic_cast","dinamični_vlitek");
        replace.put("explicit","izrecno");
        replace.put("export","izvozi");
        replace.put("extern","zunanji");
        replace.put("inline","v_vrsti");
        replace.put("mutable","utišljiva");
        replace.put("noexcept","ne_razen");
        replace.put("not","ne");
        replace.put("reinterpret_cast","reinterpretirani_vlitek");
        replace.put("requires","zahteva");
        replace.put("signed","podpisana");
        replace.put("sizeof","velikost_od");
        replace.put("static_assert","statični");
        replace.put("static_cast","statični_vlitek");
        replace.put("synchronized","sinhornizirana");
        replace.put("template","predloga");
        replace.put("thread_local","nit_lokalna");
        replace.put("throw","vrže");
        replace.put("typedef","vrsta_def");
        replace.put("typeid","vrsta_id");
        replace.put("typename","vrsta_ime");
        replace.put("union","unija");
        replace.put("unsigned","nepodpisana");
        replace.put("volatile","hlapjiva");
        replace.put("wchar_t","wznak_t");
        replace.put("xor","xali");

    }

    private static void handleconversion(JFrame guiFrame){
        final JPanel conversion = new JPanel();
        JButton convert = new JButton( "Convert");
        conversion.add(convert);

        convert.addActionListener(ae -> {
            try {
                if (file == null){
                    JOptionPane.showMessageDialog(f, fileerror, "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String content;
                content = Files.readString(Paths.get(file.getPath()));

                if (!istsk) {

                    //Replace part
                    content = translate(content, pohabljenke);
                    content = translate(content, replace);

                    //Adds extension to the end of document
                    content += "\n //DO NOT TOUCH \n" + getFileExtension(file);
                    String fileNameWithOutExt = file.getName().replaceFirst("[.][^.]+$", "");

                    //Saves
                    Files.write(Paths.get(file.getParent() + "/" + fileNameWithOutExt + ".tsk"), content.getBytes(StandardCharsets.UTF_8));

                    //Popup
                    f=new JFrame();
                    JOptionPane.showMessageDialog(f,convertedtotsk,convsuccess,JOptionPane.PLAIN_MESSAGE);
                } else {

                    //Makes reverse maps
                    Map<String, String> swapped = reverse(replace);
                    Map<String, String> nepohabljenke = reverse(pohabljenke);

                    //Replaces switched
                    content = translate(content, nepohabljenke);
                    content = translate(content, swapped);

                    //Gets file extension and location
                    String filepath = file.getParent() + "/" + file.getName() + content.substring(content.lastIndexOf("\n")).trim();

                    //Removes added extension lines
                    int truncateIndex = content.length();
                    for (int i = 0; i < 3; i++) {
                        truncateIndex = content.lastIndexOf('\n', truncateIndex - 1);
                    }
                    content = content.substring(0, truncateIndex);

                    //Saves
                    Files.write(Paths.get(filepath), content.getBytes(StandardCharsets.UTF_8));

                    //Popup
                    f=new JFrame();
                    JOptionPane.showMessageDialog(f,convbacktocode,convsuccess,JOptionPane.WARNING_MESSAGE);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }});
        guiFrame.add(conversion,BorderLayout.SOUTH);
    }

    private static String translate(String content, Map<String,String> mapa){
        ArrayList<String> prefix = new ArrayList<>();
        prefix.add("\t");
        prefix.add(" ");
        prefix.add("\n");
        prefix.add("=");
        prefix.add("\\(");
        prefix.add("#");

        ArrayList<String> suffix = new ArrayList<>();
        suffix.add(" ");
        suffix.add(";");
        suffix.add("\\{");
        suffix.add("\\(");
        suffix.add("\\)");
        suffix.add(",");
        suffix.add("\\*");

        for (String replacement : mapa.keySet())
            for(String p : prefix)
                for(String s : suffix)
                    content = content.replaceAll(p + replacement + s, p + mapa.get(replacement) + s);

        return content;
    }

    private static void handleselection(JFrame guiFrame){

        //SELECTION PART
        final JPanel selection = new JPanel();
        JLabel choosefile = new JLabel(choose);

        JButton tskornormalbutton = new JButton(convmodewaiting);
        tskornormalbutton.addActionListener(ae -> {
            if(!(tskornormalbutton.getText().equals(convmodewaiting))){
                if (!istsk) {
                    JOptionPane.showMessageDialog(f, backtoog, "Info", JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(f, totsk, "Info", JOptionPane.INFORMATION_MESSAGE);
                }
            }});

        JButton choosebutton = new JButton("File");
        choosebutton.addActionListener(ae -> {
            JFileChooser fileChooser = new JFileChooser();
            int returnVal = fileChooser.showOpenDialog(fileChooser);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = fileChooser.getSelectedFile();
                choosebutton.setText(file.getName());
                if (getFileExtension(file).equals(".tsk")) {
                    istsk = true;
                    tskornormalbutton.setText(convmodenormal);
                } else {
                    istsk = false;
                    tskornormalbutton.setText(convmodetsk);
                }
            } else {
                file = null;
            }
        });

        selection.add(choosefile);
        selection.add(choosebutton);
        selection.add(tskornormalbutton);

        guiFrame.add(selection, BorderLayout.NORTH);
    }

    private static void handleframe(JFrame guiFrame){
        guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        guiFrame.setTitle(title);
        guiFrame.setSize(500,110);
        guiFrame.setLocationRelativeTo(null);
        guiFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/res/tsk.png")));
    }

    private static String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return name.substring(lastIndexOf);
    }

    private static <K,V> HashMap<V,K> reverse(Map<K, V> map) {
        HashMap<V,K> rev = new HashMap<>();
        for(Map.Entry<K,V> entry : map.entrySet())
            rev.put(entry.getValue(), entry.getKey());
        return rev;
    }


}

