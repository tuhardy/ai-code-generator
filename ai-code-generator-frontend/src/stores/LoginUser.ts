import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import { getLoginUser } from '@/api/userController.ts'

export const useLoginUserStore = defineStore('LoginUser', () => {
  const loginUser=ref<API.LoginUserVO>({
    userName: "未登录",
  })
  //获取登录用户信息
  async function fetchLoginUser() {
    const res = await getLoginUser()
    if(res.data.code==0&&res.data.data){
      loginUser.value = res.data.data
    }
  }
  //更新登录用户信息、
  function setLoginUser(newLoginUser:any) {
    loginUser.value = newLoginUser
  }
  return { loginUser, fetchLoginUser, setLoginUser }
})
