package magicrafttg.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

import net.minecraft.entity.Entity;

import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelMCTGDireWolf extends ModelBase {

	/** main box for the wolf head */
    public ModelRenderer wolfHeadMain;
    /** The wolf's body */
    public ModelRenderer wolfBody;
    /** Wolf'se first leg */
    public ModelRenderer wolfLeg1;
    /** Wolf's second leg */
    public ModelRenderer wolfLeg2;
    /** Wolf's third leg */
    public ModelRenderer wolfLeg3;
    /** Wolf's fourth leg */
    public ModelRenderer wolfLeg4;
    /** The wolf's tail */
    ModelRenderer wolfTail;
    /** The wolf's mane */
    ModelRenderer wolfMane;
    
    public ModelMCTGDireWolf()
    {
    	textureWidth = 64;
		textureHeight = 64;
		  
		wolfHeadMain = new ModelRenderer(this, 0, 0);
		wolfHeadMain.addBox(-4F, -4F, -3F, 8, 8, 6);
		wolfHeadMain.setRotationPoint(0F, 12F, -10F);
		
	    wolfBody = new ModelRenderer(this, 0, 14);
	    wolfBody.addBox(-4.5F, -2F, -4.5F, 9, 13, 9);
	    wolfBody.setRotationPoint(0F, 12F, 0F);
	    
	    wolfMane = new ModelRenderer(this, 0, 36);
	    wolfMane.addBox(-5F, -3F, -5F, 10, 6, 10);
	    wolfMane.setRotationPoint(0F, 11.5F, -5F);
	    
	    wolfLeg1 = new ModelRenderer(this, 36, 14);
	    wolfLeg1.addBox(-1.5F, 0F, -1.5F, 3, 8, 3);
	    wolfLeg1.setRotationPoint(-2.5F, 16F, 7F);
	    
	    wolfLeg2 = new ModelRenderer(this, 36, 14);
	    wolfLeg2.addBox(-1.5F, 0F, -1.5F, 3, 8, 3);
	    wolfLeg2.setRotationPoint(2.5F, 16F, 7F);
	    
	    wolfLeg3 = new ModelRenderer(this, 36, 14);
	    wolfLeg3.addBox(-1.5F, 0F, -1.5F, 3, 8, 3);
	    wolfLeg3.setRotationPoint(-3.5F, 16F, -4F);
	    
	    wolfLeg4 = new ModelRenderer(this, 36, 14);
	    wolfLeg4.addBox(-1.5F, 0F, -1.5F, 3, 8, 3);
	    wolfLeg4.setRotationPoint(2.5F, 16F, -4F);
	    
	    wolfTail = new ModelRenderer(this, 49, 14);
	    wolfTail.addBox(-1.5F, 0F, -1.5F, 3, 9, 3);
	    wolfTail.setRotationPoint(0F, 9F, 10F);
	    
	    wolfHeadMain.setTextureOffset(53, 0).addBox(-3.5F, -6F, 0F, 3, 2, 2); // ear1
	    wolfHeadMain.setTextureOffset(53, 0).addBox(0.5F, -6F, 0F, 3, 2, 2); // ear2
	    wolfHeadMain.setTextureOffset(28, 0).addBox(-2.5F, 0F, -7F, 5, 4, 7); // nose
    }
    
    
    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity entity, float time, float limbSwingDistance, float p_78088_4_, float headYRot, float headXRot, float yTrans)
    {
        super.render(entity, time, limbSwingDistance, p_78088_4_, headYRot, headXRot, yTrans);
        this.setRotationAngles(time, limbSwingDistance, p_78088_4_, headYRot, headXRot, yTrans, entity);
        
        
        this.wolfTail.rotateAngleY = MathHelper.cos(time * 0.6662F) * 1.4F * limbSwingDistance;
        

        this.wolfHeadMain.renderWithRotation(yTrans);
        this.wolfBody.render(yTrans);
        this.wolfLeg1.render(yTrans);
        this.wolfLeg2.render(yTrans);
        this.wolfLeg3.render(yTrans);
        this.wolfLeg4.render(yTrans);
        this.wolfTail.renderWithRotation(yTrans);
        this.wolfMane.render(yTrans);
    }
    
    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    public void setRotationAngles(float time, float limbSwingDistance, float p_78087_3_, float headYRot, float headXRot, float p_78087_6_, Entity entity)
    {
        super.setRotationAngles(time, limbSwingDistance, p_78087_3_, headYRot, headXRot, p_78087_6_, entity);
        this.wolfHeadMain.rotateAngleX = headXRot / (180F / (float)Math.PI);
        this.wolfHeadMain.rotateAngleY = headYRot / (180F / (float)Math.PI);
        this.wolfTail.rotateAngleX = p_78087_3_;
        this.wolfBody.rotateAngleX = ((float)Math.PI / 2F);
        this.wolfMane.rotateAngleX = this.wolfBody.rotateAngleX;
        this.wolfLeg1.rotateAngleX = MathHelper.cos(time * 0.6662F) * 1.4F * limbSwingDistance;
        this.wolfLeg2.rotateAngleX = MathHelper.cos(time * 0.6662F + (float)Math.PI) * 1.4F * limbSwingDistance;
        this.wolfLeg3.rotateAngleX = MathHelper.cos(time * 0.6662F + (float)Math.PI) * 1.4F * limbSwingDistance;
        this.wolfLeg4.rotateAngleX = MathHelper.cos(time * 0.6662F) * 1.4F * limbSwingDistance;
    }
}
