-- -----------------------------------------
-- Add review rules that give custom checks 
-- by product category and/or product_code
-- ----------------------------------------
create table fdademo.review_rule (
    rule_id bigserial not null,
    product_category varchar(40) not null, 
    product_code varchar(7) not null,
	review_check varchar(60) not null, 
	automated_check boolean,
	reviewer_required_role varchar(255) not null default '',
	require_manual_review boolean, 
	check_weight int,
	create_date timestamp not null default current_timestamp,
	create_user varchar(40),
	inactive_date timestamp, 
	lupdate_date timestamp not null default current_timestamp,
	lupdate_user varchar(40),
	constraint review_rule_pk primary key (rule_id),
	constraint review_rule_uq unique (review_check)
);