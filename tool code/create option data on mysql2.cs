//生成votingoption所需資料 190221
using System;

class MainClass {
  public static void Main (string[] args) {
    string[] arr=new string[]{"user_a","user_b"};

   for (int topicCount = 1; topicCount <= 2; topicCount++)
        {
            for (int repeat = 0; repeat <= 3; repeat++)
            {
                 Console.WriteLine("");
                for (int a = -2; a <= 2; a++)
                {
                    for (int b = -2; b <= 2; b++)
                    {
                        for (int c = -2; c <= 2; c++)
                        {
                            for (int d = -2; d <= 2; d++)
                            {
                                int ee = 0, f = 0;
                                if (a > 0)
                                {
                                    ee += a;
                                }
                                if (b > 0)
                                {
                                    ee += b;
                                }
                                if (c > 0)
                                {
                                    ee += c;
                                }
                                if (d > 0)
                                {
                                    ee += d;
                                }
                                if (a < 0)
                                {
                                    f += a;
                                }
                                if (b < 0)
                                {
                                    f += b;
                                }
                                if (c < 0)
                                {
                                    f += c;
                                }
                                if (d < 0)
                                {
                                    f += d;
                                }
                                if (f != 0 && ((double)ee / f == 0.5 || (double)ee / f == -0.5))
                                {
                                    
                                    Console.WriteLine(string.Format("INSERT INTO `option` VALUES ('{0}',{1},'{2}','{3}','{4}','{5}','{6}','{7}','{8}',{9});"
                                    ,topicCount
                                    ,"null"
                                    ,string.Format("{0}_{1}:{2}_{3}_{4}_{5}",topicCount,repeat,a,b,c,d)
                                    ,string.Format("topic{0}_optionRepeat{1}:{2}_{3}_{4}_{5}",topicCount,repeat,a,b,c,d)
                                    ,a,b,c,d
                                    ,arr[new System.Random().Next(2)]
                                    ,"now()"
                                    ));
                                }
                            }
                        }

                    }

                }
            }
        }
  }
}