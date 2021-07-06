<template>
  <div>
    <div class="create-form">
      <el-form
        :model="dynamicValidateForm"
        :rules="createRules"
        ref="dynamicValidateForm"
        label-width="100px"
        class="demo-dynamic"
      >
        <el-form-item prop="title" label="标题">
          <el-input v-model="dynamicValidateForm.title"></el-input>
        </el-form-item>
        <el-form-item prop="desc" label="描述">
          <el-input v-model="dynamicValidateForm.desc"></el-input>
        </el-form-item>
        <el-form-item
          class="demo-form-inline"
          v-for="(chain, index) in dynamicValidateForm.itemList"
          :label="'参与方' + index"
          :key="chain.key"
        >
          <el-row>
            <el-col :span="8">
              <el-form-item
                label=""
                :prop="'itemList.' + index + '.role'"
                :rules="createRules.role"
              >
                <el-input v-model="chain.role" placeholder="角色"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item
                label=""
                :prop="'itemList.' + index + '.portion'"
                :rules="createRules.portion"
              >
                <el-input
                  type="number"
                  maxlength="2"
                  v-model="chain.portion"
                  placeholder="份额"
                >
                  <template slot="append">%</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="7">
              <el-form-item
                label=""
                :prop="'itemList.' + index + '.id'"
                :rules="createRules.id"
              >
                <el-select
                  v-model="chain.id"
                  placeholder="参与者"
                  :disabled="chain.isSelect"
                >
                  <template>
                    <el-option
                      v-for="item in roleList"
                      :label="item.orgName"
                      :value="item.id"
                      :key="item.id"
                    ></el-option>
                  </template>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="1">
              <div style="padding-left:5px" v-if="!chain.isSelect">
                <el-button type="text" @click="removeChain(chain)"
                  >移除</el-button
                >
              </div>
            </el-col>
          </el-row>
          <!--  <el-button @click.prevent="removeChain(chain)">删除</el-button> -->
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('dynamicValidateForm')"
            >提交</el-button
          >
          <el-button @click="addChain">新增</el-button>
          <!--  <el-button @click="resetForm('dynamicValidateForm')">重置</el-button> -->
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import { getParticipaterId, getUserId } from "@/utils/auth";
import { getSelectList, createChain } from "@/api/table";
import uuidv4 from "uuid/v4";
export default {
  data() {
    const isDecimal = (rule, value, callback) => {
      const reg = /^([1]?\d{1,2})$/;
      if (!reg.test(value)) {
        callback(new Error("请输入1-100之间整数"));
      } else {
        callback();
      }
    };
    return {
      createRules: {
        title: [{ required: true, message: "请输入标题", trigger: "blur" }],
        desc: [{ required: true, message: "请输入描述", trigger: "blur" }],
        role: [{ required: true, message: "请输入角色", trigger: "blur" }],
        portion: [{ validator: isDecimal, trigger: "blur" }],
        id: [{ required: true, message: "请选择参与者", trigger: "blur" }],
      },
      test: "",
      roleList: [],

      level: 1,
      nextUuid: "",
      dynamicValidateForm: {
        itemList: [
          {
            id: "",
            role: "",
            portion: "",
            itemId: this.createUuid(),
            levelOnChain: 0,
            isSelect: true,
            participaterId: "",
          },
        ],
        title: "",
        userId: "",
      },
    };
  },
  created() {
    this.getSeletData();
  },
  methods: {
    createUuid() {
      let uuid = uuidv4();
      uuid = uuid.split("-").join("");
      return uuid;
    },
    getSeletData() {
      const userId = getUserId();
      const pId = getParticipaterId();
      this.dynamicValidateForm.userId = userId;
      this.preUuid = this.dynamicValidateForm.itemList[0].itemId;
      getSelectList().then((res) => {
        this.roleList = res.data;
        this.roleList.forEach((item) => {
          if (item.id == pId) {
            this.dynamicValidateForm.itemList[0].id = item.id;
          }
        });
      });
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          const data = this.dynamicValidateForm.itemList;
          let totalRatio = data.reduce((prev, cur) => {
            return parseFloat(cur.portion) + parseFloat(prev);
          }, 0);

          if (totalRatio !== 100) {
            this.$message({
              message: "分成比例总和应为100%",
              type: "warning",
            });
          } else {
            let arrayId = [];
            data.forEach((item) => {
              arrayId.push(item.id);
            });
            const noRepeatId = [...new Set(arrayId)];

            if (arrayId.length === noRepeatId.length) {
              this.dynamicValidateForm.itemList.forEach((item, index) => {
                item.participaterId = item.id;
                item.levelOnChain = index;
              });
              console.log(this.dynamicValidateForm);
              createChain(this.dynamicValidateForm).then((res) => {
                if (res.code === 0) {
                  this.$confirm("提交成功", "提交成功", {
                    confirmButtonText: "返回",
                    type: "success",
                    showCancelButton: false,
                    closeOnClickModal: false,
                  }).then(() => {
                    this.$router.push({ path: "/list" });
                  });
                }
              });
            } else {
              this.$message({
                message: "参与者不能重复选择",
                type: "error",
              });
              return;
            }
          }
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    removeChain(item) {
      const chainIndex = this.dynamicValidateForm.itemList.indexOf(item);
      console.log(this.dynamicValidateForm.itemList.length - 1);
      console.log(chainIndex);
      if (chainIndex !== this.dynamicValidateForm.itemList.length - 1) {
        const preItem = this.dynamicValidateForm.itemList[chainIndex - 1];
        this.dynamicValidateForm.itemList[chainIndex + 1].lastItemId =
          preItem.itemId;
      }

      if (chainIndex !== -1) {
        this.dynamicValidateForm.itemList.splice(chainIndex, 1);
      }
    },
    addChain() {
      this.dynamicValidateForm.itemList.push({
        participaterId: "",
        role: "",
        portion: "",
        //levelOnChain: this.level++,
        itemId: this.createUuid(),
        lastItemId: this.preUuid,
        isSelect: false,
      });
      const preIndex = this.dynamicValidateForm.itemList.length - 1;
      this.preUuid = this.dynamicValidateForm.itemList[preIndex].itemId;
      console.log(this.dynamicValidateForm.itemList);
    },
  },
};
</script>

<style lang="scss">
.create-form {
  width: 70%;
  height: 900px;
  margin: 40px auto;
  .el-col .el-form-item__content,
  .el-input input {
    margin-left: 10px;
  }
  .el-col .el-input input {
    margin-left: 0;
  }
  .el-select {
    display: block;
  }
}
</style>
