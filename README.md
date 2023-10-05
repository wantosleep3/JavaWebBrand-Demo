# 黑马程序员JavaWeb brand-case综合案例全代码

## 实现删除和修改


>[黑马程序员JavaWeb基础教程](https://www.bilibili.com/video/BV1Qf4y1T7Hx)

  

1.按照网友的思路合并了**修改**表单和**新增**表单，复习了v-bind 动态标题变化和 双向数据绑定        
2.黑马给的删除+修改作业更加熟悉了mvn结构

## **删除**思路

- Dao层

```java
/**
	 * 删除单个
	 * @param id
	 */
	@Delete("delete from tb_brand where id=#{id}")
	void deleteById(int id);

```

- service层

  ```java
  @Override
  	public void deleteById(int id) {
  		// 2、 获取sqlSession对象
  		SqlSession sqlSession = factory.openSession();
  		// 3、获取BrandMapper
  		BrandMapper mapper = sqlSession.getMapper(BrandMapper.class);// BrandMapper 接口的类对象
  		// 4、调用方法
  		mapper.deleteById(id);
  
  		// 	5、提交事务
  		sqlSession.commit();
  
  		// 释放资源
  		sqlSession.close();
  	}
  ```

  

- web层

  - 这里我们有两种方法来进行前后端交互   post和get
    - 方法1、url的形式传到后端   注意方法名  删除单个的方法这里搞了一晚上 实在是对于mvn模式还不熟练 很折磨啊

  ```java
  	public void deleteById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  		String _id = request.getParameter("id");
  		System.out.println(_id);
  		int id = Integer.parseInt(_id);
  
  		brandService.deleteById(id);
  		response.getWriter().write("success");
  ```

  - ​		

     - 方法2、前端的id号我们也可以放到前端ajax的data的位置上，以json的形式传到后端

     ```java
      public void deleteById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      		// 	1、接收数据
      		BufferedReader br = request.getReader();// 读取请求的消息体数据
      		String params = br.readLine();// json字符串
      
      		// 转为int
      		int id = JSON.parseObject(params, Integer.class);
      		// 2、调用service删除
      		brandService.deleteById(id);
      
      		// 	3、响应成功标识
      		response.getWriter().write("success");
      
      		// System.out.println("brand.add...");
      
      	}
     ```

- brand.html

```javascript
// 删除单个
// deleteById(index,row) {        实测 deleteById(row) {  也一样成功
deleteById(row) {
    //删除的对象的id
    //弹出确认删除的提示框
    this.$confirm('此操作将删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
    }).then(() => {
        var _this = this;

        //用户点击确认按钮
        //发送ajax请求
        axios({
            method: "get",
            url: "http://localhost:8080/brand-case/brand/deleteById?id=" + row.id,//get方法
            //url: "http://localhost:8080/brand-case/brand/deleteById",   //post方法
            // data:row.id   //post方法用的
        }).then(function (resp) {
            if (resp.data == "success") {
                //表示删除成功
                //刷新页面,重新查询数据
                _this.selectAll();
                //弹出消息提示
                _this.$message({
                    message: '删除成功!',
                    type: 'success'
                });
            }
        })

    }).catch(() => {
        //用户点击取消按钮
        this.$message({
            type: 'info',
            message: '已取消删除'
        });
    });
},
```





## 修改

- Dao层

```java
@Update("update tb_brand set brand_name =#{brandName}, company_name =#{companyName},ordered=#{ordered},description=#{description},status=#{status}   where id =#{id}")
	@ResultMap("brandResultMap")
	void updateByBrand(Brand brand);

```

- service层

  ```java
  @Override
  // 记得改好名字，因为此处没在代码中体现被引用  也不报错 到时候网页浏览器就报错
  public void updateByBrand(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  
      // 	1、接收品牌数据
      BufferedReader br = request.getReader();// 读取请求的消息体数据
      String params = br.readLine();// json字符串
  
      // 转为brand对象
      Brand brand = JSON.parseObject(params, Brand.class);
      // 2、调用service修改
  
      brandService.updateByBrand(brand);
  
      // 	3、响应成功标识
      response.getWriter().write("success");
  
  
  	}
  ```

  

- web层

  ```java
  @Override
  public void updateByBrand(Brand brand) {
      // 2、 获取sqlSession对象
      SqlSession sqlSession = factory.openSession();
      // 3、获取BrandMapper
      BrandMapper mapper = sqlSession.getMapper(BrandMapper.class);// BrandMapper 接口的类对象
  
      // 4、调用方法
      mapper.updateByBrand(brand);
      // 	5、提交事务
      sqlSession.commit();
  
      // 释放资源
      sqlSession.close();
  }
  ```

  

- brand.html

  - 这里我把修改的弹窗和增加的弹窗合并了，判断是哪个按钮点击的就可以了

```javascript
   
                
   <!--按钮-->

    <el-row>

        <el-button type="danger" plain @click="deleteByIds">批量删除</el-button>
        <el-button type="primary" plain @click="popUpForm('add',row)">新增</el-button>

    </el-row>





    <!--增加添加数据 or  修改数据对话框表单       修改具有数据回显功能-->
    <!--黑马教的插值表达式的写法，注意:title前面的引号  这样才能动态变化标题  <a v-bind:herf="url">简写为 <a :herf="url"> 这样就动态变化了    -->

    <el-dialog
            :title='titleMap[dialogTitle]'
            :visible.sync="dialogVisible"
            width="30%"
    >
        <!--偷懒，就不建立新的模型了，修改数据的时候背景的数据显示在修改，取消的时候我设置了重新查询一次所以不影响。-->
        <el-form ref="form" :model="brand"  class="demo-brand" label-width="80px">
            <el-form-item label="品牌名称">
                <el-input v-model="brand.brandName"></el-input>
            </el-form-item>

            <el-form-item label="企业名称">
                <el-input v-model="brand.companyName"></el-input>
            </el-form-item>

            <el-form-item label="排序">
                <el-input v-model="brand.ordered"></el-input>
            </el-form-item>

            <el-form-item label="备注">
                <el-input type="textarea" v-model="brand.description"></el-input>
            </el-form-item>

            <el-form-item label="状态">
                <el-switch v-model="brand.status"
                           active-value="1"
                           inactive-value="0"
                ></el-switch>
            </el-form-item>


            <el-form-item>
                <el-button type="primary" @click="AddOrUpdatesubmit">提交</el-button>
                <el-button @click="dialogVisible = false;selectAll()">取消</el-button>
            </el-form-item>
        </el-form>

    </el-dialog>


```



- 此方法：判断是增加还是修改？

  ```javascript
    // 判断我要弹出一个什么样的表单 是修改还是新增
  popUpForm(methodType, row) {
      this.brand = row;
      console.log(row.id);
      this.dialogVisible = true;
      if (methodType == 'add') {
          this.brand = {};  //清空品牌
          this.dialogTitle = "add";
      }
      if (methodType == 'update') {
          this.brand = row; //回显当前行品牌
          this.dialogTitle = "update";
      }
      // console.log(this.brand);
  },
      //成功提示
      success(string) {
          this.selectAll();
          this.$message({
              message: string + '成功!',
              type: 'success'
          });
      },
  
    AddOrUpdatesubmit() {
       if (this.dialogTitle == "add") {
           var _this = this;
           axios({
               method: "POST",
               url: "http://localhost:8080/brand-case/brand/add",
               data: _this.brand
           }).then(resp => {
               if (resp.data == "success") {
                   //添加成功
                   _this.selectAll();
                   //关闭窗口
                   _this.dialogVisible = false;
                   //添加成功提示
                   _this.success("数据添加");
               }
           })
       } else if (this.dialogTitle == "update") {
           console.log(this.brand);
           var _this = this;
           axios({
               method: "POST",
               url: "http://localhost:8080/brand-case/brand/updateByBrand",
               data: _this.brand
           }).then(function (resp) {
               if (resp.data == "success") {
                   //修改成功
                   _this.dialogVisible = false;
                   _this.selectAll();
                   _this.success("数据修改");
               }
           })
       }
   },
  ```

  - 增加数据

```javascript
data() {
            return {
                //合并了添加和修改的弹出框，  修改可以回显
                dialogTitle:"",

                titleMap: {
                    add: "添加数据",
                    update: "修改数据"
                },
```

