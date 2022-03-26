package top.aidan.community.entity;

import lombok.Data;

/**
 * Created by Aidan on 2021/10/14 13:57
 * GitHub: github.com/huaxin0304
 * Blog: aidanblog.top
 *
 * @author Aidan
 */

@Data
public class AidanTest {
   private String name="123";
   private int age;

   public AidanTest() {
   }

   public AidanTest(String name) {
      this.name = name;
   }

   public AidanTest(String name, int age) {
      this.name = name;
      this.age = age;
   }
}
