# quick-excel  
## 项目使用说明  
一、读取Excel文件内容  
&nbsp;&nbsp;&nbsp;&nbsp;1、创建Excel文件内容的实体类    
&nbsp;&nbsp;&nbsp;&nbsp;创建实体类需要继承BaseModel，主要是做标识使用，并且保存了当前处理的行号。  
&nbsp;&nbsp;&nbsp;&nbsp;实体类中的字段使用ExcelReadProperty注解标识，通过columnIndex指定字段与Excel文件中列的对应关系。
columnName只做标识作用，通过parseStrategy指定解析策略，默认为BaseParseStrategy解析策略（不做任何处理），如果需要自定义
解析策略需要继承Strategy类，实现parse方法。  
&nbsp;&nbsp;&nbsp;&nbsp;代码参考如下所示：
      
      public class StudentDO extends BaseModel {
          /**
           * 姓名
           */
          @ExcelReadProperty(columnIndex = 1, columnName = "姓名")
          private String name;
      
          /**
           * 年龄
           */
          @ExcelReadProperty(columnIndex = 0, columnName = "年龄")
          private String age;
      
          /**
           * 年级
           */
          @ExcelReadProperty(columnIndex = 2, columnName = "年级")
          private String grade;
      
          public String getName() {
              return name;
          }
      
          public void setName(String name) {
              this.name = name;
          }
      
          public String getAge() {
              return age;
          }
      
          public void setAge(String age) {
              this.age = age;
          }
      
          public String getGrade() {
              return grade;
          }
      
          public void setGrade(String grade) {
              this.grade = grade;
          }
      
          @Override
          public String toString() {
              return "StudentDO{" +
                      "name='" + name + '\'' +
                      ", age='" + age + '\'' +
                      ", grade='" + grade + '\'' +
                      '}';
          }
      }
      
      
     /**
      * 时间格式单元格 解析策略
      *
      * @author tomxin
      * @date 2019-01-24
      * @since v1.0.0
      */
     public class DateParseStrategy implements Strategy {
     
         /**
          * 事件格式解析策略
          *
          * @param content
          * @return
          */
         @Override
         public Object parse(Object content) {
             ContentMeta contentMeta = (ContentMeta)content;
             // 判断当前单元格的内容是否属于number类型
             if (XSSFDataTypeEnum.NUMBER.equals(contentMeta.getXssfDataTypeEnum())) {
                 // 将double类型转换为Date类型
                 Date date = DateUtil.getJavaDate((Double)ConvertUtils.convert(content, Double.class));
                 // 转换为字符串
                 return DateUtils.format(date);
             }
             return contentMeta.getContent();
         }
     }
     
    
  2、主要代码逻辑：需要将自定义的实体类，Excel文件类型xls|xlsx（最好是xlsx），Excel文件的InputStream
  赋值到ReadExcelBuilder中，调用read方法，解析到的Excel文件内容就会写入到postProcess方法中。
  可以自定义处理逻辑，因为这里使用的是事件处理（SAX）的方式来读取Excel文件内容（防止DOM解析大的Excel文件带来的问题），
  所以是每解析一条推送一条数据。
  
          ReadExcelBuilder readExcelBuilder = ExcelBuilder.of(ReadExcelBuilder::new)
              .with(ReadExcelBuilder::setExcelTypeEnum, ExcelTypeEnum.XLSX)
              .with(ReadExcelBuilder::setModelClazz, TeacherReadDO.class)
              .with(ReadExcelBuilder::setInputStream, inputStream)
              .build()
              .init();
          List<TeacherReadDO> teacherReadDOS = new ArrayList<>();
          // 读取文件内容
          readExcelBuilder.read(new ExcelEventListener() {
              @Override
              public void postProcess(Object result) {
                  TeacherReadDO teacherReadDO = (TeacherReadDO)result;
                  System.out.println("teacherReadDO " + teacherReadDO.rowNumber + " result " + result);
                  teacherReadDOS.add(teacherReadDO);
              }
          });
          // 后续逻辑处理
          .......
  二、写入Excel文件内容  
&nbsp;&nbsp;&nbsp;&nbsp;1、自定义实体类  
&nbsp;&nbsp;&nbsp;&nbsp;自定义的实体类需要继承BaseModel，做标识使用。  
&nbsp;&nbsp;&nbsp;&nbsp;通过注解ExcelWriteProperty定义实力类属性与Excel文件列的对应关系。
columnName指定列名（生成标题行），columnIndex指定对应的列号。     
                    
    public class TeacherDO extends BaseModel {
  
      /**
       * 姓名
       */
      @ExcelWriteProperty(columnName = "姓名", columnIndex = 0)
      private String name;
  
      /**
       * 课程
       */
      @ExcelWriteProperty(columnName = "课程", columnIndex = 1)
      private String course;
  
      public String getName() {
          return name;
      }
  
      public void setName(String name) {
          this.name = name;
      }
  
      public String getCourse() {
          return course;
      }
  
      public void setCourse(String course) {
          this.course = course;
      }
    }
 主要代码逻辑：                    
 &nbsp;&nbsp;&nbsp;&nbsp;1、初始化WriteExcelBuilder，需要传入的参数Excel文件类型，OutputStream。  
 &nbsp;&nbsp;&nbsp;&nbsp;2、定义ExcelSheet，写入Sheet的信息描述：Sheet名称、是否需要标题行，待写入的实体类，Sheet的序号。  
 &nbsp;&nbsp;&nbsp;&nbsp;3、调用write方法，将ExcelSheet，实体类列表 传入。  
 &nbsp;&nbsp;&nbsp;&nbsp;4、调用writeExcelBuilder.flush()，结束写入。自定义的OutputStream可另做使用。  
 &nbsp;&nbsp;&nbsp;&nbsp;5、可支持多个Sheet写入，每个Sheet写入的内容可以是不同的实体类。  
 &nbsp;&nbsp;&nbsp;&nbsp;6、支持同一个Sheet多次调用write方法写入。
 
      WriteExcelBuilder writeExcelBuilder = ExcelBuilder.of(WriteExcelBuilder::new)
                .with(WriteExcelBuilder::setExcelTypeEnum, ExcelTypeEnum.XLSX)
                .with(WriteExcelBuilder::setOutputStream, fileOutputStream)
                .build()
                .init();
        ExcelSheet oneSheet = new ExcelSheet();
        oneSheet.setSheetName("one");
        oneSheet.setNeedHeader(true);
        oneSheet.setModelClass(TeacherDO.class);
        oneSheet.setIndex(0);

        List<TeacherDO> oneTeacherList = new ArrayList<>();
        for (int i = 0; i < 10000; ++i) {
            TeacherDO teacherDO = new TeacherDO();
            teacherDO.setName("one " + i);
            teacherDO.setCourse("course " + i);
            oneTeacherList.add(teacherDO);
        }
        writeExcelBuilder.write(oneTeacherList, oneSheet);

        ExcelSheet twoSheet = new ExcelSheet();
        twoSheet.setSheetName("three");
        twoSheet.setNeedHeader(true);
        twoSheet.setModelClass(TeacherDO.class);
        twoSheet.setIndex(0);

        List<TeacherDO> threeTeacherList = new ArrayList<>();
        for (int i = 0; i < 10000; ++i) {
            TeacherDO teacherDO = new TeacherDO();
            teacherDO.setName("three " + i);
            teacherDO.setCourse("course " + i);
            threeTeacherList.add(teacherDO);
        }

        writeExcelBuilder.write(threeTeacherList, twoSheet);

        ExcelSheet threeSheet = new ExcelSheet();
        threeSheet.setSheetName("two");
        threeSheet.setNeedHeader(true);
        threeSheet.setModelClass(TeacherDO.class);
        threeSheet.setIndex(0);

        List<TeacherDO> twoTeacherList = new ArrayList<>();
        for (int i = 0; i < 10000; ++i) {
            TeacherDO teacherDO = new TeacherDO();
            teacherDO.setName("two " + i);
            teacherDO.setCourse("course " + i);
            twoTeacherList.add(teacherDO);
        }

        writeExcelBuilder.write(twoTeacherList, threeSheet);

        writeExcelBuilder.flush();

