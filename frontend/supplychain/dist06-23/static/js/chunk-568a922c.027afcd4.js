(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-568a922c"],{"07f0":function(t,n,e){},"951b":function(t,n,e){"use strict";e.r(n);var a=function(){var t=this,n=t.$createElement,e=t._self._c||n;return e("div",{staticClass:"app-container"},[e("el-form",{ref:"form",staticClass:"form-box",attrs:{model:t.form,"label-width":"120px"}},[e("el-form-item",{attrs:{label:"链路选择"}},[e("el-select",{attrs:{placeholder:"链路选择"},on:{change:t.getRole},model:{value:t.form.chainId,callback:function(n){t.$set(t.form,"chainId",n)},expression:"form.chainId"}},t._l(t.signData,(function(t){return e("el-option",{key:t.chainId,attrs:{label:t.title,value:t.chainId}})})),1)],1),e("el-form-item",[e("el-button",{attrs:{type:"primary"},on:{click:t.handleSigned}},[t._v("提交")]),e("el-button",[t._v("取消")])],1)],1),e("el-dialog",{attrs:{title:"提示",visible:t.txHashdialogVisible,width:"30%"},on:{"update:visible":function(n){t.txHashdialogVisible=n}}},[e("div",{staticClass:"trade-tips"},[e("i",{staticClass:"el-icon-circle-check",staticStyle:{clolor:"green"}}),t._v("签名成功 ")]),e("div",{staticClass:"title"},[t._v("交易hash:")]),e("span",[t._v(t._s(t.txHash))]),e("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[e("el-button",{attrs:{type:"primary"},on:{click:function(n){t.txHashdialogVisible=!1}}},[t._v("确 定")])],1)])],1)},i=[],r=(e("159b"),e("ad8f")),c=e("5f87"),o={data:function(){return{txHashdialogVisible:!1,signData:[],relationRole:[],txHash:"",form:{chainId:"",participaterId:""}}},created:function(){this.getSignData()},methods:{getSignData:function(){var t=this,n=Object(c["c"])();Object(r["g"])(n).then((function(n){t.signData=n.data,t.form.chainId=t.signData[0].chainId}))},getRole:function(t){var n=this,e=t;this.signData.forEach((function(t){if(t.chainId===e)return n.relationRole=t.roles,void(n.form.participaterId=t.roles[0].participaterId)}))},handleSigned:function(){var t=this,n=Object(c["a"])();this.form.participaterId=parseInt(n);var e=this.form;Object(r["a"])(e).then((function(n){if(0===n.code){var e=n.data.txHash;t.txHash=e,t.txHashdialogVisible=!0}}))}}},s=o,u=(e("e925"),e("2877")),l=Object(u["a"])(s,a,i,!1,null,"3ad94156",null);n["default"]=l.exports},ad8f:function(t,n,e){"use strict";e.d(n,"e",(function(){return i})),e.d(n,"d",(function(){return r})),e.d(n,"b",(function(){return c})),e.d(n,"f",(function(){return o})),e.d(n,"c",(function(){return s})),e.d(n,"g",(function(){return u})),e.d(n,"a",(function(){return l})),e.d(n,"j",(function(){return d})),e.d(n,"h",(function(){return h})),e.d(n,"i",(function(){return f}));var a=e("b775");e("5f87");function i(t){return Object(a["a"])({url:"/supplychain/chain/"+t,method:"get"})}function r(t){return Object(a["a"])({url:"/supplychain/chain/join/"+t,method:"get"})}function c(t){return Object(a["a"])({url:"/supplychain/chain/checkSign/"+t,method:"get"})}function o(){return Object(a["a"])({url:"/supplychain/participater/list",method:"get"})}function s(t){return Object(a["a"])({url:"/supplychain/chain/new",method:"post",data:t})}function u(t){return Object(a["a"])({url:"/supplychain/chain/getInfo/"+t,method:"get"})}function l(t){return Object(a["a"])({url:"/supplychain/chain/sign",method:"post",data:t})}function d(t){return Object(a["a"])({url:"/supplychain/chain/pay",method:"post",data:t})}function h(){return Object(a["a"])({url:"/supplychain/user/getUnLoginTotalInfo",method:"get"})}function f(t){return Object(a["a"])({url:"/supplychain/user/"+t,method:"get"})}},e925:function(t,n,e){"use strict";e("07f0")}}]);