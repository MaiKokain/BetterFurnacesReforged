# Fork Changes
- Adds "Processing Upgrade" 
  - Doubles any item amount
  - can stack to 64
  - output calculation: `(RecipeOutput * ProcessingUpgradeAmount)`
    - eg: recipe output is `2`, you have `64` processing upgrade, the calculation would be `2 * 64 = 128` so the item output would be `128`


- Allows chaning configuration of "Fuel Efficiency" upgrade, both normal and advanced
  - Can be configured up to `Integer.MAX_VALUE`, floating point
  - fuel burn time calculation: `(FuelEfficiencyStat * FurnaceTierSpeed)`
    - eg: configured efficiency stat `3.0`,  furnaceSpeed: `150` (iron tier), it would be `3.0 * 150 = 450` so the fuel burn time would be `450`

# Current issue
Output slot bugging out if item amount is higher than 64, this wasn't an issue with `Furnace Overhaul` so not fixing. Pipe it to a chest or something.

# BetterFurnacesReforged
Resume

This mod project plans to rewrite the old Better Furnaces mod for the new versions, adapting it to the new Minecraft concepts, but without leaving its old look.

Objective and Thanks I have no financial interests in this, the fun and knowledge acquired to do it are the most important. Parts of code from o

ther open source mods were used for certain elements. The Furnaces Overhaul mod code base was completely used to make version 1.12, without it it wouldn't be possible to have this level of optimization and features of the new update