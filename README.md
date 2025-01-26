# LazyShuffle
Addon for SkinShuffle mod that allows you to load skins from the folder

## How to Use This Mod

### Step-by-Step Instructions:

1. **Add Your Skins**:
   Place your skins in the folder:  
   `config/skinshuffle/skins/lazyshuffle`.

2. **Choose the Correct Model**:
   - To use the **Classic (Steve)** skin: Ensure the file name does not contain `_slim`.
   - To use the **Slim (Alex)** skin: Add `_slim` to the file name.

   **Examples**:
   - For the **Classic** model: `skinname.png`
   - For the **Slim** model: `skinname_slim.png`

3. **Apply Changes**:
   After completing the above steps, press the **Update List** button to apply the changes!


## How to Use Backups

1. Navigate to the folder:  
   `config/skinshuffle/backup`.
2. Locate the latest backup file.
3. Close Minecraft.
4. Rename the backup file to `presets.json` and replace the existing `presets.json` file in:  
   `config/skinshuffle`.


## Attention!

### Important Warnings:
1. **Avoid Crashes**:
   - Do not open the presets if you have removed a skin from the `config/skinshuffle/skins/lazyshuffle` folder.
   - Opening the presets after removing a skin may cause a **NullPointerException** crash in Minecraft.

2. **How to safely remove a skins**:
   - Close Minecraft.
   - Remove the skin file from the folder.
   - Press the **Update Skins** button to reload the skins.
