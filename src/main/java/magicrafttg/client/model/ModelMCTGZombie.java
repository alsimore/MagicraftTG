package magicrafttg.client.model;

import net.minecraft.client.model.ModelZombie;
import net.minecraft.entity.Entity;

public class ModelMCTGZombie extends ModelZombie {

	public ModelMCTGZombie()
	{
		super();
	}
	
	public void render(Entity entity, float time, float limbSwingDistance, float p_78088_4_, float headYRot, float headXRot, float yTrans)
    {
        super.render(entity, time, limbSwingDistance, p_78088_4_, headYRot, headXRot, yTrans);
    }
	
	public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_)
    {
        super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, p_78087_7_);
    }
}
