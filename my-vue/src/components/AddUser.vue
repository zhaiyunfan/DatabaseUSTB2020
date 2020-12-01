<template>
  <div class="hello">
    <el-dialog title="添加一篇小说" :visible.sync="dialogAdd.show">
      <el-form :model="formData" ref="formdong" label-width="100px" :rules="formRules">
        <el-form-item label="标题" prop="novelName">
          <el-input v-model="formData.novelName"></el-input>
        </el-form-item>
        <el-form-item label="作者" prop="writer">
          <el-input v-model="formData.writer"></el-input>
        </el-form-item>
        <el-form-item label="简介" prop="summary">
          <el-input v-model="formData.summary"></el-input>
        </el-form-item>
        <el-form-item label="类型" prop="genre">
          <el-input v-model="formData.genre"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogAdd.show = false">取 消</el-button>
        <el-button type="primary" @click="dialogFormAdd('formdong')">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'AddUser',
  props: {
    dialogAdd: Object
  },
  data() {
    return {
      formData: {
        novelName: '',
        writer: '',
        summary: '',
        genre: ''
      },
      formRules: {
        novelName: [{required: true, message: "小说名称不能为空", trigger: "blur"}],
        genre: [{required: true, message: "类型不能为空", trigger: "blur"}],
      }
    }
  },
  methods: {
    dialogFormAdd(formdong) {
      console.log(this.formData)
      this.$refs[formdong].validate((valid) => {
        if (valid) {
          let params = new URLSearchParams();
          params.append('novelName',this.formData.novelName)
          params.append('writer',this.formData.writer)
          params.append('summary',this.formData.summary)
          params.append('genre',this.formData.genre)
          this.$axios.post('novel/create', params).then(res => {
            this.$message({
              type: "success",
              message: "添加书籍成功"
            })
            this.dialogAdd.show = false;
            this.$emit('update');
            console.log(res)
          })
          this.formDate = ""
        } else {
          console.log('填写字段不完整');
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