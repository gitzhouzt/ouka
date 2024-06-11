<template>
  <div class="w-full text-14px">
    <n-checkbox v-model:checked="checked">OKします</n-checkbox>
    <n-button :text="true" type="primary" @click="handleClickProtocol">「利用規約」</n-button>
    <n-button :text="true" type="primary" @click="handleClickPolicy">「プライバシーポリシー」</n-button>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';

interface Props {
  /** is selected */
  value?: boolean;
}

interface Emits {
  (e: 'update:value', value: boolean): void;
  /** click 「利用規約」 */
  (e: 'click-protocol'): void;
  /** click 「プライバシーポリシー」 */
  (e: 'click-policy'): void;
}

const props = withDefaults(defineProps<Props>(), {
  value: true
});

const emit = defineEmits<Emits>();

const checked = computed({
  get() {
    return props.value;
  },
  set(newValue: boolean) {
    emit('update:value', newValue);
  }
});

function handleClickProtocol() {
  emit('click-protocol');
}
function handleClickPolicy() {
  emit('click-policy');
}
</script>
<style scoped></style>
