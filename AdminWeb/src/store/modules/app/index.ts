import { nextTick } from 'vue';
import { defineStore } from 'pinia';

interface AppState {
  /** Reload the page (control the display of the page) */
  reloadFlag: boolean;
  /** Drawer visible state of project configuration */
  settingDrawerVisible: boolean;
  /** Sidebar collapsed state */
  siderCollapse: boolean;
  /** Fixed state of sidebar in vertical-mix mode */
  mixSiderFixed: boolean;
}

export const useAppStore = defineStore('app-store', {
  state: (): AppState => ({
    reloadFlag: true,
    settingDrawerVisible: false,
    siderCollapse: false,
    mixSiderFixed: false
  }),
  actions: {
    /**
     * reload
     * @param duration - Reload delay time (ms)
     */
    async reloadPage(duration = 0) {
      this.reloadFlag = false;
      await nextTick();
      if (duration) {
        setTimeout(() => {
          this.reloadFlag = true;
        }, duration);
      } else {
        this.reloadFlag = true;
      }
      setTimeout(() => {
        document.documentElement.scrollTo({ left: 0, top: 0 });
      }, 100);
    },
    /** Open the settings drawer */
    openSettingDrawer() {
      this.settingDrawerVisible = true;
    },
    /** Close the settings drawer */
    closeSettingDrawer() {
      this.settingDrawerVisible = false;
    },
    /** Toggle drawer visible state */
    toggleSettingDrawerVisible() {
      this.settingDrawerVisible = !this.settingDrawerVisible;
    },
    /** Set the sidebar collapsed state */
    setSiderCollapse(collapse: boolean) {
      this.siderCollapse = collapse;
    },
    /** Collapse/Expand Sidebar collapsed state */
    toggleSiderCollapse() {
      this.siderCollapse = !this.siderCollapse;
    },
    /** Set the fixed state of the sidebar in vertical-mix mode */
    setMixSiderIsFixed(isFixed: boolean) {
      this.mixSiderFixed = isFixed;
    },
    /** Set the fixed state of the sidebar in vertical-mix mode */
    toggleMixSiderFixed() {
      this.mixSiderFixed = !this.mixSiderFixed;
    }
  }
});
