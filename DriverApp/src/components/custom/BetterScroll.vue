<template>
	<div ref="bsWrap" class="h-full text-left">
		<div ref="bsContent" class="inline-block" :class="{ 'h-full': !isScrollY }">
			<slot></slot>
		</div>
	</div>
</template>

<script setup lang="ts">
import { ref, computed, watch, onMounted } from 'vue';
import { useElementSize } from '@vueuse/core';
import BScroll from '@better-scroll/core';
import type { Options } from '@better-scroll/core';

interface Props {
	/** better-scroll config: https://better-scroll.github.io/docs/zh-CN/guide/base-scroll-options.html */
	options: Options;
}

const props = defineProps<Props>();

const bsWrap = ref<HTMLElement>();
const instance = ref<BScroll>();
const bsContent = ref<HTMLElement>();
const isScrollY = computed(() => Boolean(props.options.scrollY));

function initBetterScroll() {
	if (!bsWrap.value) return;
	instance.value = new BScroll(bsWrap.value, props.options);
}

// scroll at change time， update BS
const { width: wrapWidth } = useElementSize(bsWrap);
const { width, height } = useElementSize(bsContent);
watch([() => wrapWidth.value, () => width.value, () => height.value], () => {
	if (instance.value) {
		instance.value.refresh();
	}
});

onMounted(() => {
	initBetterScroll();
});

defineExpose({ instance });
</script>
<style scoped></style>
