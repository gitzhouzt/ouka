<template>
	<div v-if="showTooltip">
		<n-tooltip :placement="placement" trigger="hover">
			<template #trigger>
				<div
					class="flex-center h-full cursor-pointer dark:hover:bg-[#333]"
					:class="computedClass"
				>
					<slot></slot>
				</div>
			</template>
			{{ tooltipContent }}
		</n-tooltip>
	</div>
	<div
		v-else
		class="flex-center cursor-pointer dark:hover:bg-[#333]"
		:class="computedClass"
	>
		<slot></slot>
	</div>
</template>

<script lang="ts" setup>
import { computed } from 'vue';
import type { FollowerPlacement } from 'vueuc';

interface Props {
	/** tooltip show content */
	tooltipContent?: string;
	/** tooltip position */
	placement?: FollowerPlacement;
	/** content className */
	contentClass?: string;
	/** inverted */
	inverted?: boolean;
}
const props = withDefaults(defineProps<Props>(), {
	tooltipContent: '',
	placement: 'bottom',
	contentClass: '',
	inverted: false,
});

const showTooltip = computed(() => Boolean(props.tooltipContent));

const computedClass = computed(() =>
	[
		props.contentClass,
		props.inverted ? 'hover:bg-primary' : 'hover:bg-[#f6f6f6]',
	].join(' ')
);
</script>
<style scoped></style>
