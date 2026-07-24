# Eon Lazy Patch

面向 `Minecraft Java Edition 1.21.1` / `NeoForge` 的轻量整合包补丁模组。

这个模组的目标很直接：把休闲养老、建筑、装备提升过程中那些重复、繁琐、但又很适合自动化的流程收束起来。它不是大型内容模组，更像一组面向个人整合包的实用工具：减少不喜欢的手工环节，同时保留一点阶段感和合成门槛。

## 当前版本

- Minecraft: `1.21.1`
- NeoForge: `21.1.243`
- Mod ID: `eon_lazy_patch`
- Version: `1.1.0-1.21.1`
- License: `MIT`

## 主要内容

### Eon 终端装备

- 添加 Eon 珍珠，配方需要 4 个究极精华、4 个红物质和 1 个下界之星。
- 添加 Eon 头盔、胸甲、护腿和靴子。
- 添加 Eon 剑、镐、斧和铲。
- 装备不可破坏、抗火，并采用面向整合包终局阶段的高属性。
- 在锻造台中使用对应下界合金装备和 Eon 珍珠进行升级，不需要锻造模板。

Eon 珍珠配方只在同时安装 `Mystical Agriculture` 和 `ProjectE` 时启用。

### ProjectE EMC 适配

为 AE2 和 ExtendedAE 的基础材料提供整合包级 EMC 修正：

| 材料 | EMC |
| --- | ---: |
| 赛特斯石英水晶、充能赛特斯石英水晶和赛特斯石英粉 | 256 |
| 福鲁伊克斯水晶和福鲁伊克斯粉 | 288 |
| 硅 | 256 |
| 末影粉 | 1024 |
| 熵变水晶和熵变粉 | 1024 |
| 熵变碎片 | 128 |
| 熵变水晶块 | 4096 |
| 熵变锭 | 3936 |

熵变种子、芽体和生长簇不提供 EMC，玩家仍需先完成 ExtendedAE 的材料培育流程。

### 经验系统

#### 经验水晶

- 容量：`1,000,000 XP`
- 右键空气：从水晶取出最多 30 级跨度的经验
- 潜行右键：向水晶存入最多 30 级跨度的经验
- 右键可抽取经验流体的方块：从方块抽取经验流体并转化为水晶经验
- Tooltip 显示当前经验 / 最大经验
- 物品耐久条显示储能进度

#### 经验灌注器

- 可储存经验流体
- GUI 中放入经验水晶后自动充能
- 支持管道输入经验流体
- 会在 tooltip 中显示具体流体和换算后的 XP

### 能源系统

#### 恒能发电机

- 凭空发电，定位为中期省心能源
- 发电：`1024 FE/t`
- 输出：`4096 FE/t`
- 缓存：`4,000,000 FE`
- 支持向相邻方块和管道主动输出能量
- GUI 中可放入能量物品充电
- 合成需要满电 `mekanism:energy_tablet`

#### 创造能源立方合成

在安装 Mekanism 时启用。

合成需要：

- 4 个下界合金块
- 2 个下界之星
- 1 个恒能发电机
- 2 个满电终极能量立方

合成产物为满电的 `mekanism:creative_energy_cube`。

### AE2 无限流体元件

在安装 `AE2` 以及对应流体来源模组时启用。

当前包含：

- `ME 无限液态幽匿物质元件`
- `ME 无限液态龙息元件`
- `ME 无限以太气体元件`
- `ME 无限粉色史莱姆液元件`

每种无限元件由：

- `ae2:fluid_cell_housing`
- 对应的校准核心

合成得到。

校准核心使用 `64k ME 存储组件`、高级机器框架、钻石、粉色史莱姆锭和对应主题材料合成。

## 可选适配

这些模组不是强制前置。未安装时，对应内容不会注册或不会显示对应配方。

- `Applied Energistics 2`
- `Industrial Foregoing`
- `Industrial Foregoing: More Upgrades`
- `Mekanism`
- `Mystical Agriculture`
- `ProjectE`
- `ExtendedAE`

## 开发与构建

### 普通构建

```bash
./gradlew build
```

### 生成数据资源

项目使用 DataGen 生成配方、模型、语言文件、方块状态、战利品表和标签：

```bash
./gradlew runData
```

生成结果位于：

```text
src/generated/resources
```

本地测试用的整合包 jar 放在 `libs/` 下。`runClient` / `runServer` / `runGameTestServer` 启动前会自动把这些 jar 复制到 `run/mods`，让 NeoForge 像正常游戏一样从 mods 文件夹扫描它们；`runData` 不会加载这些 jar，以避免其他模组的数据生成逻辑干扰本模组。

## 仓库结构说明

- `src/main/java`：模组源码
- `src/main/resources`：手工维护的资源，主要是贴图、GUI 材质和模板文件
- `src/generated/resources`：DataGen 生成资源
- `block_bench_files`：本地贴图源文件目录，默认不提交
- `libs`：本地测试依赖目录，默认不提交

## 许可与素材声明

本项目代码使用 [MIT License](LICENSE)。

项目中有部分 GUI 资源参考并转存自 MIT 许可的模组资源，来源与说明见 [THIRD_PARTY_NOTICES.md](THIRD_PARTY_NOTICES.md)。

## 设计定位

Eon Lazy Patch 的目标不是追求严格平衡，而是服务于休闲整合包体验：让已经证明自己有生产能力的阶段更快进入自动化和建筑节奏。它适合想少做重复劳动、多做自己喜欢内容的存档。
