//生成user_option所需資料，其中a()、b()等應隨option_id變化 190222
using System;
using System.Collections.Generic;

class MainClass {
  public static void Main (string[] args) {
      int a=3675;
      int b=4106;
      int topic_cnt=2;
      int option_cnt=(b-a)/topic_cnt;
      Console.WriteLine("# a="+a+",b="+b+",option_cnt:"+option_cnt);
      Console.WriteLine("# "+a);
      run(1,a);
for (int j=0;j<topic_cnt;j++){
    int c=a+(option_cnt*j)+1;
    int d=b-(option_cnt*(topic_cnt-1-j));
    Console.WriteLine("# "+c+" "+(d-1));
    for (int i=c;i<d;i++){
        run(j+1,i);
    }
}
Console.WriteLine("# "+b);
run(topic_cnt,b);
  }
  public static void run (int topic_id,int option_id) {
int l=new System.Random().Next(1,3+1);
        // Console.WriteLine("l:"+l+"");
        Console.WriteLine("");
        List<string> users = new List<string>();
        users.Add("user_a");
        users.Add("user_b");
        users.Add("user_c");
        for(int k=0;k<l;k++){
            int m=new System.Random().Next(users.Count);
             Console.WriteLine(string.Format("insert into `user_option` values('{0}','{1}','{2}',{3},{4});"
             ,users[m],topic_id+"",option_id+"","now()","now()"));
             users.RemoveAt(m);
        }
  }
}