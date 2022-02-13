package lt.wayout.minecraft.plugin.wayengine.packet.entity;

import org.bukkit.entity.EntityType;

import javax.annotation.Nullable;

public enum PacketLivingEntityType {
    ARMOR_STAND(1),
    AXOLOTL(3),
    BAT(4),
    BEE(5),
    BLAZE(6),
    CAT(8),
    CAVE_SPIDER(9),
    CHICKEN(10),
    COD(11),
    COW(12),
    CREEPER(13),
    DOLPHIN(14),
    DONKEY(15),
    DROWNED(17),
    ELDER_GUARDIAN(18),
    ENDER_DRAGON(20),
    ENDERMAN(21),
    ENDERMITE(22),
    EVOKER(23),
    FOX(29),
    GHAST(30),
    GIANT(31),
    GLOW_SQUID(33),
    GOAT(34),
    GUARDIAN(35),
    HOGLIN(36),
    HORSE(37),
    HUSK(38),
    ILLUSIONER(39),
    IRON_GOLEM(40),
    LLAMA(46),
    MAGMA_CUBE(48),
    MULE(57),
    MOOSHROOM(58),
    OCELOT(59),
    PANDA(61),
    PARROT(62),
    PHANTOM(63),
    PIG(64),
    PIGLIN(65),
    PIGLIN_BRUTE(66),
    PILLAGER(67),
    POLAR_BEAR(68),
    PUFFERFISH(70),
    RABBIT(71),
    RAVAGER(72),
    SALMON(73),
    SHEEP(74),
    SHULKER(75),
    SILVERFISH(77),
    SKELETON(78),
    SKELETON_HORSE(79),
    SLIME(80),
    SNOW_GOLEM(82),
    SPIDER(85),
    SQUID(86),
    STRAY(87),
    STRIDER(88),
    TRADER_LLAMA(94),
    TROPICAL_FISH(95),
    TURTLE(96),
    VEX(97),
    VILLAGER(98),
    VINDICATOR(99),
    WANDERING_TRADER(100),
    WITCH(101),
    WITHER(102),
    WITHER_SKELETON(103),
    WOLF(105),
    ZOGLIN(106),
    ZOMBIE(107),
    ZOMBIE_HORSE(108),
    ZOMBIE_VILLAGER(109),
    ZOMBIFIED_PIGLIN(110);

    private final int typeId;
    PacketLivingEntityType(int typeId) {
        this.typeId = typeId;
    }

    public int getTypeId() {
        return typeId;
    }

    @Nullable
    public static PacketLivingEntityType fromBukkit(EntityType type) {
        return switch (type) {
            case ARMOR_STAND -> PacketLivingEntityType.ARMOR_STAND;
            case AXOLOTL -> PacketLivingEntityType.AXOLOTL;
            case BAT -> PacketLivingEntityType.BAT;
            case BEE -> PacketLivingEntityType.BEE;
            case BLAZE -> PacketLivingEntityType.BLAZE;
            case CAT -> PacketLivingEntityType.CAT;
            case CAVE_SPIDER -> PacketLivingEntityType.CAVE_SPIDER;
            case CHICKEN -> PacketLivingEntityType.CHICKEN;
            case COD -> PacketLivingEntityType.COD;
            case COW -> PacketLivingEntityType.COW;
            case CREEPER -> PacketLivingEntityType.CREEPER;
            case DOLPHIN -> PacketLivingEntityType.DOLPHIN;
            case DONKEY -> PacketLivingEntityType.DONKEY;
            case DROWNED -> PacketLivingEntityType.DROWNED;
            case ELDER_GUARDIAN -> PacketLivingEntityType.ELDER_GUARDIAN;
            case ENDER_DRAGON -> PacketLivingEntityType.ENDER_DRAGON;
            case ENDERMAN -> PacketLivingEntityType.ENDERMAN;
            case ENDERMITE -> PacketLivingEntityType.ENDERMITE;
            case EVOKER -> PacketLivingEntityType.EVOKER;
            case FOX -> PacketLivingEntityType.FOX;
            case GHAST -> PacketLivingEntityType.GHAST;
            case GIANT -> PacketLivingEntityType.GIANT;
            case GLOW_SQUID -> PacketLivingEntityType.GLOW_SQUID;
            case GOAT -> PacketLivingEntityType.GOAT;
            case GUARDIAN -> PacketLivingEntityType.GUARDIAN;
            case HOGLIN -> PacketLivingEntityType.HOGLIN;
            case HORSE -> PacketLivingEntityType.HORSE;
            case HUSK -> PacketLivingEntityType.HUSK;
            case ILLUSIONER -> PacketLivingEntityType.ILLUSIONER;
            case IRON_GOLEM -> PacketLivingEntityType.IRON_GOLEM;
            case LLAMA -> PacketLivingEntityType.LLAMA;
            case MAGMA_CUBE -> PacketLivingEntityType.MAGMA_CUBE;
            case MULE -> PacketLivingEntityType.MULE;
            case MUSHROOM_COW -> PacketLivingEntityType.MOOSHROOM;
            case OCELOT -> PacketLivingEntityType.OCELOT;
            case PANDA -> PacketLivingEntityType.PANDA;
            case PARROT -> PacketLivingEntityType.PARROT;
            case PHANTOM -> PacketLivingEntityType.PHANTOM;
            case PIG -> PacketLivingEntityType.PIG;
            case PIGLIN -> PacketLivingEntityType.PIGLIN;
            case PIGLIN_BRUTE -> PacketLivingEntityType.PIGLIN_BRUTE;
            case PILLAGER -> PacketLivingEntityType.PILLAGER;
            case POLAR_BEAR -> PacketLivingEntityType.POLAR_BEAR;
            case PUFFERFISH -> PacketLivingEntityType.PUFFERFISH;
            case RABBIT -> PacketLivingEntityType.RABBIT;
            case RAVAGER -> PacketLivingEntityType.RAVAGER;
            case SALMON -> PacketLivingEntityType.SALMON;
            case SHEEP -> PacketLivingEntityType.SHEEP;
            case SHULKER -> PacketLivingEntityType.SHULKER;
            case SILVERFISH -> PacketLivingEntityType.SILVERFISH;
            case SKELETON -> PacketLivingEntityType.SKELETON;
            case SKELETON_HORSE -> PacketLivingEntityType.SKELETON_HORSE;
            case SLIME -> PacketLivingEntityType.SLIME;
            case SNOWMAN -> PacketLivingEntityType.SNOW_GOLEM;
            case SPIDER -> PacketLivingEntityType.SPIDER;
            case SQUID -> PacketLivingEntityType.SQUID;
            case STRAY -> PacketLivingEntityType.STRAY;
            case STRIDER -> PacketLivingEntityType.STRIDER;
            case TRADER_LLAMA -> PacketLivingEntityType.TRADER_LLAMA;
            case TROPICAL_FISH -> PacketLivingEntityType.TROPICAL_FISH;
            case TURTLE -> PacketLivingEntityType.TURTLE;
            case VEX -> PacketLivingEntityType.VEX;
            case VILLAGER -> PacketLivingEntityType.VILLAGER;
            case VINDICATOR -> PacketLivingEntityType.VINDICATOR;
            case WANDERING_TRADER -> PacketLivingEntityType.WANDERING_TRADER;
            case WITCH -> PacketLivingEntityType.WITCH;
            case WITHER -> PacketLivingEntityType.WITHER;
            case WITHER_SKELETON -> PacketLivingEntityType.WITHER_SKELETON;
            case WOLF -> PacketLivingEntityType.WOLF;
            case ZOGLIN -> PacketLivingEntityType.ZOGLIN;
            case ZOMBIE -> PacketLivingEntityType.ZOMBIE;
            case ZOMBIE_HORSE -> PacketLivingEntityType.ZOMBIE_HORSE;
            case ZOMBIE_VILLAGER -> PacketLivingEntityType.ZOMBIE_VILLAGER;
            case ZOMBIFIED_PIGLIN -> PacketLivingEntityType.ZOMBIFIED_PIGLIN;
            default -> null;
        };
    }
}
