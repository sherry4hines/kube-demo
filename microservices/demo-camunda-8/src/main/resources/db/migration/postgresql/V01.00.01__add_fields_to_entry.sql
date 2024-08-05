-- --------------------------------------------------
-- Add Review summary fields to humanFoodEntry table
-- --------------------------------------------------
ALTER TABLE fdademo.shipment_entry_line 
ADD COLUMN consolidate_review_score int, 
ADD COLUMN manual_review_required boolean,
ADD COLUMN process_instance_id varchar(64)
;