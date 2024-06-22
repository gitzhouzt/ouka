<template>
	<p>
		<span>{{ label }}</span>
		<a class="text-blue-500" :href="link" target="_blank">
			{{ link }}
		</a>
	</p>
</template>

<script setup lang="ts">
interface Props {
	/** site name */
	label: string;
	/** site link */
	link: string;
}

defineProps<Props>();
</script>
<style scoped></style>
