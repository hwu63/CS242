package com.example.why.assignment3;
import android.os.Parcel;
import android.os.Parcelable;

public class Item_user implements Parcelable  {

    private String Username;
    private String Avatar;
    private String User_Url;
    private String Html_Url;
    private String Email;
    private String Follow_Url;
    private String Following_Url;
    private String Repo_Url;
    private String Description;


    public Item_user(String username, String avatar, String user_url, String html_url,
                     String follow_url, String following_url, String repo_url) {
        this.Username = username;
        this.Avatar = avatar;
        this.User_Url = user_url;
        this.Html_Url = html_url;
        this.Follow_Url = follow_url;
        this.Following_Url = following_url;
        this.Repo_Url = repo_url;

    }

    public String getItemUsername() {
        return this.Username;
    }

    public String getItemAvatar() {
        return this.Avatar;
    }

    public String getItemUrl() {
        return this.User_Url;
    }

    public String getItemHtml_Url() {
        return this.Html_Url;
    }

    public String getItemEmail() {
        return this.Email;
    }

    public String getItemFollow_Url() {
        return this.Follow_Url;

    }

    public String getItemFollowing_Url() {
        return this.Following_Url;

    }

    public String getItemRepo_Url() {
        return  this.Repo_Url;
    }

    public String getItemDescription() {
        return  this.Description;
    }

    public void setDescription(String des){
        Description = des;
    }

    public void setEmail(String email){
        Email = email;
    }


    public String print(){
        return Username +"  "+
                Avatar +"  "+
                User_Url +"  "+
                Html_Url +"  "+
                Email +"  "+
                Follow_Url +"  "+
                Following_Url +"  "+
                Repo_Url;
    }


    // Parcelling part
    public Item_user(Parcel in){
        String[] data = new String[9];

        in.readStringArray(data);
        // the order needs to be the same as in writeToParcel() method
        this.Username = data[0];
        this.User_Url = data[1];
        this.Avatar = data[2];
        this.Html_Url = data[3];
        this.Email = data[4];
        this.Follow_Url = data[5];
        this.Following_Url = data[6];
        this.Repo_Url = data[7];
        this.Description = data[8];
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.Username,
                this.Avatar,
                this.User_Url,
                this.Html_Url,
                this.Email,
                this.Follow_Url,
                this.Following_Url,
                this.Repo_Url,
                this.Description});
    }
    public static final Creator CREATOR = new Creator() {
        public Item_user createFromParcel(Parcel in) {
            return new Item_user(in);
        }

        public Item_user[] newArray(int size) {
            return new Item_user[size];
        }
    };
}
