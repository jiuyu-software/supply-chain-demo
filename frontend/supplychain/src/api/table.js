import request from "@/utils/request";
import { getUserId } from "@/utils/auth";
//const userId = getUserId();
export function getList(userId) {
  return request({
    url: "/supplychain/chain/" + userId,
    method: "get",
  });
}
export function getJoinList(userId) {
  return request({
    url: "/supplychain/chain/join/" + userId,
    method: "get",
  });
}

export function checkSign(chainId) {
  return request({
    url: "/supplychain/chain/checkSign/" + chainId,
    method: "get",
  });
}
export function getSelectList() {
  return request({
    url: "/supplychain/participater/list",
    method: "get",
  });
}
export function createChain(data) {
  return request({
    url: "/supplychain/chain/new",
    method: "post",
    data,
  });
}
export function getSignInfo(userId) {
  return request({
    url: "/supplychain/chain/getInfo/" + userId,
    method: "get",
  });
}
export function addSign(data) {
  return request({
    url: "/supplychain/chain/sign",
    method: "post",
    data,
  });
}
export function toPay(data) {
  return request({
    url: "/supplychain/chain/pay",
    method: "post",
    data,
  });
}
export function getTotalInfo() {
  return request({
    url: "/supplychain/user/getUnLoginTotalInfo",
    method: "get",
  });
}
export function getUser(userId) {
  return request({
    url: "/supplychain/user/" + userId,
    method: "get",
  });
}
