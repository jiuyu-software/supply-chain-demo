<template>
  <div class="app-container">
    <!-- <div class="btn-top">
      <el-button @click="createChain" type="primary" plain>新建</el-button>
    </div> -->
    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
    >
      <el-table-column align="center" label="序号" width="95">
        <template slot-scope="scope">
          {{ scope.$index }}
        </template>
      </el-table-column>
      <el-table-column label="公司名" width="190" show-overflow-tooltip>
        <template slot-scope="scope">
          {{ scope.row.orgName }}
        </template>
      </el-table-column>
      <el-table-column
        label="链名称"
        width="110"
        align="center"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <span>{{ scope.row.title }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="描述"
        width="110"
        align="center"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          {{ scope.row.desc }}
        </template>
      </el-table-column>
      <el-table-column label="状态" width="80" align="center">
        <template slot-scope="scope">
          <div v-if="scope.row.status == 'confirmed'">
            <el-tag size="small">已确认</el-tag>
          </div>
          <div v-else>
            <el-tag type="info" size="small">草稿</el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="合约地址" width="360" align="center">
        <template slot-scope="scope">
          {{ scope.row.contractAddress }}
        </template>
      </el-table-column>
      <el-table-column label="存证KEY" width="360" align="center">
        <template slot-scope="scope">
          {{ scope.row.evidenceKey }}
        </template>
      </el-table-column>
      <!-- <el-table-column label="查看供应商" width="110" align="center">
        <template slot-scope="scope">
          <el-button
            size="mini"
            @click="handleSupplier(scope.$index, scope.row)"
            >查看</el-button
          >
        </template>
      </el-table-column> -->
      <el-table-column label="操作" width="210" align="center" fixed="right">
        <template slot-scope="scope">
          <a class="btn-link" @click="handleSupplier(scope.$index, scope.row)"
            >查看参与方</a
          ><span class="individer-line">|</span>
          <a class="btn-link" @click="handleSign(scope.$index, scope.row)"
            >查看签名状态</a
          >
        </template>
      </el-table-column>
    </el-table>
    <el-dialog title="供应商信息" :visible.sync="dialogSupplierVisible">
      <el-table :data="supplierData">
        <el-table-column
          property="participaterDidId"
          label="参与者数字身份"
          width=""
        ></el-table-column>
        <el-table-column
          property="participaterOrgName"
          label="参与者公司名"
          width="200"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column label="分成比例" width="90">
          <template slot-scope="scope"
            >{{ scope.row.portion }}%</template
          ></el-table-column
        >
        <el-table-column
          property="role"
          width="100"
          label="角色"
          show-overflow-tooltip
        ></el-table-column>
      </el-table>
    </el-dialog>
    <!-- 签名状态弹框 -->
    <el-dialog
      title="签名状态"
      :visible.sync="dialogSignVisible"
      width="30%"
      class="sign-box"
    >
      <div class="content">
        <el-divider content-position="left"
          ><i class="el-icon-check"></i>已签名({{ signedCount }})</el-divider
        >
        <div class="list">
          <template v-if="signData.signedOrgNames">
            <span v-for="item in signData.signedOrgNames" :key="item">
              <span>{{ item }}</span>
              <el-divider direction="vertical"></el-divider
            ></span>
          </template>
        </div>
        <el-divider content-position="left" class="need-sign-title"
          ><i class="el-icon-edit"></i>需要签名({{ needSignCount }})</el-divider
        >
        <span
          class="list"
          v-for="(item, index) in signData.needSignOrgNames"
          :key="index"
        >
          <span>{{ item }}</span>
          <el-divider direction="vertical"></el-divider>
        </span>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getJoinList, checkSign } from "@/api/table";
import { getUserId } from "@/utils/auth";

export default {
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: "success",
        draft: "gray",
        deleted: "danger",
      };
      return statusMap[status];
    },
  },
  data() {
    return {
      list: null,
      listLoading: true,
      supplierData: [],
      dialogSupplierVisible: false,
      dialogSignVisible: false,
      signData: {
        needSignOrgNames: [],
        signedOrgNames: [],
      },
      needSignCount: "",
      signedCount: "",
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    createChain() {
      this.$router.push("/list/create");
    },
    handleSign(index, row) {
      const { chainId } = row;
      checkSign(chainId).then((response) => {
        this.dialogSignVisible = true;
        this.signData = response.data;
        this.needSignCount = this.signData.needSignOrgNames.length;
        this.signedCount = this.signData.signedOrgNames.length;
      });
    },
    handleSupplier(index, row) {
      this.dialogSupplierVisible = true;
      this.supplierData = row.itemList;
    },
    fetchData() {
      const id = getUserId();
      this.listLoading = true;
      getJoinList(id).then((response) => {
        this.list = response.data;
        this.listLoading = false;
      });
    },
  },
};
</script>
<style lang="scss">
.btn-top {
  padding-bottom: 20px;
}
.btn-link {
  color: #2d8cf0;
}
.individer-line {
  padding: 0 10px;
  color: #c2c2c2;
}
.el-divider--horizontal {
  margin: 35px 0;
}
.el-table th {
  background: #f8f8f9;
  color: #515a6e;
}
.sign-box {
  .need-sign-title {
    margin-top: 20px;
  }
  .el-dialog__body {
    padding-bottom: 60px;
    padding-top: 0;
  }
}
</style>
