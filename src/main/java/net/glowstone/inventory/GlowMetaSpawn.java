package net.glowstone.inventory;

import net.glowstone.util.nbt.CompoundTag;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.meta.SpawnMeta;

import java.util.Map;

public class GlowMetaSpawn extends GlowMetaItem implements SpawnMeta {
    private EntityType type;

    public GlowMetaSpawn(GlowMetaItem meta) {
        super(meta);

        if (!(meta instanceof GlowMetaSpawn))
            return;

        GlowMetaSpawn spawn = (GlowMetaSpawn) meta;
        if (spawn.hasEntityType()) {
            this.type = spawn.getEntityType();
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = super.serialize();
        result.put("meta-type", "MONSTER_EGG");
        if (hasEntityType()) {
            result.put("entity-id", getEntityType().getId());
        }
        return result;
    }

    @Override
    void writeNbt(CompoundTag tag) {
        super.writeNbt(tag);
        if (hasEntityType()) {
            CompoundTag entity = new CompoundTag();
            entity.putString("id", getEntityType().getId());
            tag.putCompound("EntityTag", entity);
        }
    }

    @Override
    void readNbt(CompoundTag tag) {
        super.readNbt(tag);
        if (tag.isCompound("EntityTag")) {
            CompoundTag entity = tag.getCompound("EntityTag");
            if (entity.isString("id")) {
                for (EntityType entityType : EntityType.values()) {
                    if (entityType.getId() == null) continue;
                    if (entityType.getId().equalsIgnoreCase(entity.getString("id"))) {
                        type = entityType;
                    }
                }
            }
        }
    }

    @Override
    public boolean isApplicable(Material material) {
        return material == Material.MONSTER_EGG;
    }

    @Override
    public boolean hasEntityType() {
        return this.type != null;
    }

    @Override
    public EntityType getEntityType() {
        return this.type;
    }

    @Override
    public void setEntityType(EntityType type) {
        this.type = type;
    }

    @Override
    public GlowMetaSpawn clone() {
        return new GlowMetaSpawn(this);
    }
}
