<template>
  <div class="hello">
    <el-dialog title="添加章节" :visible.sync="dialogEdit.show">
      <el-form :model="form" ref="formEdit" label-width="100px" :rules="formRules">
<!--        <el-form-item label="待编辑小说名称" prop="novelName">-->
<!--          <el-input v-model="form.novelName"></el-input>-->
<!--        </el-form-item>-->
        <el-form-item label="待插入章节序号" prop="chaptersNum">
          <el-input v-model="form.chaptersNum"></el-input>
        </el-form-item>
        <el-form-item label="正文" prop="mainText">
          <el-input v-model="form.mainText"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogEdit.show = false">取 消</el-button>
        <el-button type="primary" @click="dialogFormEdit('formEdit')">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'HelloWorld',
  props:{
    dialogEdit:Object,
    form:Object
  },
  data () {
    return {
      formRules:{
        chaptersNum:[{required:true,message:"待插入章节序号不能为空",trigger:"blur"}],
        mainText:[{required:true,message:"正文不能为空",trigger:"blur"}],
      }
    }
  },
  methods:{
    dialogFormEdit(formEdit) {
      this.$refs[formEdit].validate((valid) => {
        if (valid) {
          let params = new URLSearchParams();
          params.append('novelName',this.form.novelName)
          params.append('chaptersNum',this.form.chaptersNum)
          params.append('text',this.form.mainText)
          this.$axios.post(`novel/add`,params).then(res => {
            this.$message({
              type:"success",
              message:"插入章节成功"
            })
            console.log(res)
            this.dialogEdit.show = false;
            this.$emit('updateEdit')
          })
        } else {
          console.log('error submit!!');
          return false;
        }
      })
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>