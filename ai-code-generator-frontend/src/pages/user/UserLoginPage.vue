<template>
  <div id="userLoginPage">
    <h2 class="title">AI智能代码生成器</h2>
    <div class="desc">
      <p>欢迎使用AI智能代码生成器，请输入您的账号密码登录</p>
    </div>
    <a-form
      :model="formState"
      name="basic"
      autocomplete="off"
      @finish="handleSubmit"
    >
      <a-form-item name="userAccount" :rules="[{ required: true, message: '请输入账号' }]">
        <a-input v-model:value="formState.userAccount" />
      </a-form-item>

      <a-form-item
        name="userPassword"
        :rules="[
          { required: true, message: '请输入密码' },
          { min: 6, message: '密码长度至少6位' },
        ]"
      >
        <a-input-password v-model:value="formState.userPassword" />
      </a-form-item>

      <div class="tips">
        <!--        没有账号？<a href="/user/register">立即注册</a>-->
        没有账号？<router-link to="/user/register">立即注册</router-link>
      </div>
      <a-form-item>
        <a-button type="primary" html-type="submit" style="width: 100%">Submit</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>
<script lang="ts" setup>
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useLoginUserStore } from '@/stores/LoginUser.ts'
import { userLogin } from '@/api/userController.ts'
import { message } from 'ant-design-vue'
const router = useRouter()
const loginUserStore = useLoginUserStore()
const formState = reactive<API.UserLoginRequest>({
  userAccount: '',
  userPassword: '',
})
/**
 * 提交表单
 */
const handleSubmit = async (values: any) => {
  const res = await userLogin(values)
  //登录成功，把登录信息保存在全局状态store中
  if (res.data.code == 0 && res.data.data) {
    await loginUserStore.fetchLoginUser()
    message.success('登录成功')
    router.push({
      path: '/',
      replace: true,
    })
  } else {
    message.error('登录失败，请检查账号密码' + res.data.message)
  }
}
</script>
<style scoped>
#userLoginPage {
  max-width: 360px;
  margin: 0 auto;
}

.title {
  text-align: center;
  margin-bottom: 16px;
}

.desc {
  text-align: center;
  color: #bbb;
  margin-bottom: 16px;
}

.tips {
  margin-bottom: 16px;
  color: #bbb;
  font-size: 13px;
  text-align: right;
}
</style>
