<template>
  <div class="app-container">
    <el-form ref="form" :model="form" label-width="120px" class="form-box">
      <el-form-item label="链路选择">
        <el-select
          v-model="form.chainId"
          placeholder="链路选择"
          @change="getRole"
        >
          <el-option
            v-for="item in signData"
            :label="item.title"
            :value="item.chainId"
            :key="item.chainId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="角色选择">
        <el-select v-model="form.participaterId" placeholder="角色选择">
          <el-option
            v-for="item in relationRole"
            :label="item.role"
            :value="item.participaterId"
            :key="item.participaterId"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSigned">提交</el-button>
        <el-button>取消</el-button>
      </el-form-item>
    </el-form>
    <el-dialog title="提示" :visible.sync="txHashdialogVisible" width="30%">
      <div class="trade-tips">
        <i class="el-icon-circle-check" style="clolor:green"></i>签名成功
      </div>
      <div class="title">交易hash:</div>
      <span>{{ txHash }}</span>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="txHashdialogVisible = false"
          >确 定</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getSignInfo, addSign } from "@/api/table";
import { getUserId } from "@/utils/auth";

export default {
  data() {
    return {
      txHashdialogVisible: false,
      signData: [],
      relationRole: [],
      txHash: "",
      form: {
        chainId: "",
        participaterId: "",
      },
    };
  },
  created() {
    this.getSignData();
  },
  methods: {
    getSignData() {
      const id = getUserId();

      getSignInfo(id).then((res) => {
        this.signData = res.data;
        this.form.chainId = this.signData[0].chainId;
        this.form.participaterId = this.signData[0].roles[0].participaterId;
        this.relationRole = this.signData[0].roles;
      });
    },
    getRole(chainId) {
      const id = chainId;
      this.signData.forEach((item) => {
        if (item.chainId === id) {
          this.relationRole = item.roles;
          this.form.participaterId = item.roles[0].participaterId;
          return;
        }
      });
    },
    handleSigned() {
      const params = this.form;
      addSign(params).then((res) => {
        if (res.code === 0) {
          const Tx_hash = res.data.txHash;
          this.txHash = Tx_hash;
          this.txHashdialogVisible = true;
        }
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.trade-tips {
  font-size: 16px;
  padding-bottom: 30px;
  text-align: center;
  color: #000;
  i {
    color: #67c23a;
    padding-right: 10px;
    font-size: 18px;
  }
}
.el-select {
  width: 100%;
}
.form-box {
  width: 40%;
}
</style>
