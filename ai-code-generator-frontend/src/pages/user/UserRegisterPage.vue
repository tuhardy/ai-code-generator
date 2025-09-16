<template>
  <div id="userRegisterPage">
    <h2 class="title">AI智能代码生成器</h2>
    <div class="desc">
      <p>欢迎使用AI智能代码生成器，请填写信息完成注册</p>
    </div>
    <a-form
      :model="formState"
      name="register"
      autocomplete="off"
      @finish="handleSubmit"
    >
      <a-form-item name="userAccount" :rules="[{ required: true, message: '请输入账号' }]">
        <a-input v-model:value="formState.userAccount" placeholder="请输入账号" />
      </a-form-item>

      <a-form-item
        name="userPassword"
        :rules="[
          { required: true, message: '请输入密码' },
          { min: 6, message: '密码长度至少6位' },
        ]"
      >
        <a-input-password v-model:value="formState.userPassword" placeholder="请输入密码" />
      </a-form-item>

      <a-form-item
        name="checkPassword"
        :rules="[
          { required: true, message: '请确认密码' },
          { validator: validatePassword },
        ]"
      >
        <a-input-password v-model:value="formState.checkPassword" placeholder="请确认密码" />
      </a-form-item>

      <div class="tips">
        已有账号？<router-link to="/user/login">立即登录</router-link>
      </div>
      <a-form-item>
        <a-button type="primary" html-type="submit" style="width: 100%">注册</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>
<script lang="ts" setup>
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import { userRegister } from '@/api/userController.ts'
import { message } from 'ant-design-vue'

const router = useRouter()

const formState = reactive<API.UserRegisterRequest>({
  userAccount: '',
  userPassword: '',
  checkPassword: '',
})

/**
 * 验证两次密码是否一致
 */
const validatePassword = async (_rule: any, value: string) => {
  if (value === '') {
    return Promise.reject('请确认密码')
  }
  if (value !== formState.userPassword) {
    return Promise.reject('两次输入的密码不一致')
  }
  return Promise.resolve()
}

/**
 * 提交注册表单
 */
const handleSubmit = async (values: any) => {
  try {
    const res = await userRegister(values)
    if (res.data.code === 0) {
      message.success('注册成功')
      // 注册成功后自动跳转到登录页面
      router.push({
        path: '/user/login',
        replace: true,
      })
    } else {
      message.error('注册失败：' + res.data.message)
    }
  } catch (error) {
    message.error('注册失败，请稍后重试')
    console.error('注册错误', error)
  }
}
</script>
<style scoped>
#userRegisterPage {
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