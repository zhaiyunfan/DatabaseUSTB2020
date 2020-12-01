<template>
  <div class="info">
    <h1>开源小说共享平台</h1>
    <el-row>
      <el-col :span="20" :push='2'>
        <div>
          <el-form :inline="true">
            <el-form-item style="float: left" label="查询指定类型小说:">
              <el-input v-model="keyUser" placeholder="请输入小说类型"></el-input>
            </el-form-item>
            <el-form-item style="float: right">
              <el-button type="primary" size="small" icon="el-icon-edit-outline" @click="hanldeAdd()">添加小说</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div class="table">
          <el-table
              :data=tableData
              border
              style="width: 100%">
            <el-table-column
                type="index"
                label="编号"
                align="center"
                width="60">
            </el-table-column>
            <el-table-column
                label="修改日期"
                align="center"
                width="120">
              <template slot-scope="scope">
                <span>{{ scope.row.editDate }}</span>
              </template>
            </el-table-column>
            <el-table-column
                label="名称"
                align="center"
                width="200">
              <template slot-scope="scope">
                <span>{{ scope.row.novelName }}</span>
              </template>
            </el-table-column>
            <el-table-column
                label="作者"
                align="center"
                width="150">
              <template slot-scope="scope">
                <span>{{ scope.row.writer }}</span>
              </template>
            </el-table-column>
            <el-table-column
                label="简介"
                align="center"
                width="800">
              <template slot-scope="scope">
                <span>{{ scope.row.summary }}</span>
              </template>
            </el-table-column>
            <el-table-column
                label="总章数"
                align="center"
                width="100">
              <template slot-scope="scope">
                <span>{{ scope.row.totalChapters }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" fixed="right">
              <template slot-scope="scope">
                <el-button
                    size="mini"
                    type="danger"
                    @click="handleEdit(scope.$index, scope.row)">添加章节
                </el-button>
                <el-button
                    size="mini"
                    type="danger"
                    @click="handleRead(scope.row.novelName,scope.row.totalChapters)">阅读章节
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-col>
    </el-row>
    <AddUser :dialogAdd="dialogAdd" @update="getUserInfo"></AddUser>
    <EditUser :dialogEdit="dialogEdit" :form="form" @updateEdit="getUserInfo"></EditUser>
    <el-dialog
        title="提示"
        :visible.sync="dialogVisible"
        width="30%"
        :before-close="handleClose">
      <div>
        {{dialogText}}
      </div>
      <span slot="footer" class="dialog-footer">
    <el-button @click="dialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
  </span>
    </el-dialog>
  </div>
</template>

<script>
import AddUser from './AddUser.vue'
import EditUser from './EditUser.vue'

export default {
  name: 'info',
  data() {
    return {
      dialogText:'',
      dialogVisible: false,
      tableData: [],
      dialogEdit: {
        show: false,
      },
      dialogAdd: {
        show: false
      },
      keyUser: "",
      form: {    //编辑信息
        editDate: '',
        novelName: '',
        writer: '',
        summary: '',
        totalChapters: 0,
      },
    }
  },
  methods: {
    handleClose(done) {
      this.$confirm('确认关闭？')
          .then(_ => {
            console.log(_)
            done();
          })
          .catch(_ => {console.log(_)});
    },
    getUserInfo() {
      //wr3ck97y.xiaomy.net:30244/springbootdemo/
      this.$axios.get('novel/search?genre=all').then(res => {
        console.log(res)
        this.tableData = res.data.novelsList
      })
    },
    hanldeAdd() {  //添加
      this.dialogAdd.show = true;
    },
    handleEdit(index, row) {  //编辑
      this.dialogEdit.show = true; //显示弹窗
      this.form = {
        novelName: row.novelName,
        chaptersNum: row.totalChapters + 1,
        mainText: '',
        editDate: row.editDate,
        writer: row.writer,
        summary: row.summary
      }
      console.log(row)
    },
    handleRead(novelName, totalChapters) {
      this.$prompt('请输入要阅读的章节号', '正文', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(({value}) => {
        console.log(value);
        console.log(novelName)
        if (value > totalChapters) {
          this.$message({
            type: "fail",
            message: "超出总章数限制"
          })
          return false
        } else {
          console.log('novel/chapter?novelName='+{novelName}+'&chaptersNum='+{value})
          this.$axios.get('novel/chapter?novelName='+novelName+'&chaptersNum='+value).then(res => {
            this.dialogVisible = true
            console.log(res.data.chaptersList.text)
            this.dialogText = res.data.chaptersList.text
            this.$message({
              type: "success",
              message: "打开章节成功"
            })
            console.log(res)

          })

        }
      });
      // 删除用户信息
      // this.$axios.delete(`http://localhost:3000/data/${row.id}`).then(res =>{
      //   this.$message({
      //     type:"success",
      //     message:"删除信息成功"
      //   })
      //   this.getUserInfo()    //删除数据，更新视图
      // })
    },
    searchUserinfo(keyUser) {
      return this.tableData.filter((user) => {
        if (user.name.includes(keyUser)) {
          return user
        }
      })
    }
  },
  created() {
    this.getUserInfo()
  },
  components: {
    AddUser,
    EditUser
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h1 {
  font-size: 30px;
  color: #333;
  text-align: center;
  margin: 0 auto;
  padding-bottom: 5px;
  border-bottom: 2px solid #409EFF;
  width: 300px
}
</style>