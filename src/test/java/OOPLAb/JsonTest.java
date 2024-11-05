package OOPLAb;
import com.google.gson.Gson;


import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import com.google.gson.annotations.SerializedName;

class wik_page{
    @SerializedName("ns")
    int ns;
    @SerializedName("title")
    String title;
    @SerializedName("pageid")
    int pageid;
    @SerializedName("size")
    int size;
    @SerializedName("wordcount")
    int wordcount;
    @SerializedName("snippet")
    String snippet;
    @SerializedName("timestamp")
    String timestamp;

    void print(){
        System.out.println(this.title+" ("+this.pageid+")");
    }
}
class Continue {
    @SerializedName("sroffset")
    public float sroffset;
    @SerializedName("continue")
    public String con;
}
class Query {
    @SerializedName("searchinfo")
    Searchinfo SearchinfoObject;
    @SerializedName("search")
    List<wik_page> search;
    public void print(){
        System.out.println("Всего статей найдено: "+this.search.size());
        int i=1;
        for(wik_page page : this.search){
            System.out.print(i+": ");
            page.print();
            i++;
        }
    }
}
class Searchinfo {
    @SerializedName("totalhits")
    private float totalhits;
    @SerializedName("suggestion")
    private String suggestion;
    @SerializedName("suggestionsnippet")
    private String suggestionsnippet;
}
class AnsvJson {
    @SerializedName("batchcomplete")
    public String batchcomplete;
    @SerializedName("continue")
    Continue ContinueObject;
    @SerializedName("query")
    Query QueryObject;
}

class NodeAnsvJson{
    AnsvJson body;
    URL request_url;
    URI article_url;
    public NodeAnsvJson(String request) throws IOException {
        String urlAddres="https://ru.wikipedia.org/w/api.php?action=query&list=search&utf8=&format=json&srsearch=";
        String str=URLEncoder.encode(request, StandardCharsets.UTF_8);
        urlAddres=urlAddres+str;
        URL wik_request_url;
        this.article_url=null;
        try {
            wik_request_url = new URI(urlAddres).toURL();
            this.request_url=wik_request_url;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        HttpURLConnection connection= null;
        int rescode;
        try {
            connection = (HttpURLConnection) wik_request_url.openConnection();
            connection.setRequestMethod("GET");
            rescode=connection.getResponseCode();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(rescode==200){
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = reader.readLine();
            this.body = new Gson().fromJson(line, AnsvJson.class);
            reader.close();
        }else{
            System.out.println("Ошибка в запросе");
            System.exit(0);
        }


    }
    public void print(){
        System.out.println("Ссылка запроса:");
        System.out.println(this.request_url.toString());
        this.body.QueryObject.print();
    }
    void openArticl() throws URISyntaxException, IOException {
        this.print();
        int i=-1;
        String urlAddres = "";

        while (true){
            System.out.println("Выберите статью");
            Scanner in=new Scanner(System.in);
            i = in.nextInt();
            if(i<1||(i >this.body.ContinueObject.sroffset)){
                System.out.println("не верный номер");
            }else{
                Integer num=this.body.QueryObject.search.get(i - 1).pageid;
                urlAddres = "https://ru.wikipedia.org/w/index.php?curid=" +num.toString();
                break;
            }


        }
        article_url=new URI(urlAddres);
        System.out.println("Ссылка на статью:\n"+article_url.toString());
        System.out.println("Открыть ссылку(Y/N)?");
        Scanner in=new Scanner(System.in);
        String flag = in.nextLine();
        if(Objects.equals(flag, "Y")) {
            Desktop bros = Desktop.getDesktop();
            bros.browse(article_url);
        }

    }


}

public class JsonTest {
    public static void main(String[] args) throws IOException, URISyntaxException {
        System.out.println("Введите запрос:");
        Scanner in=new Scanner(System.in);
        String str = in.nextLine();
        NodeAnsvJson main=new NodeAnsvJson(str);
        main.openArticl();
    }
}