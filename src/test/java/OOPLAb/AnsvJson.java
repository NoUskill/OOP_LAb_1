package OOPLAb;

import com.google.gson.annotations.SerializedName;

import java.util.List;


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
