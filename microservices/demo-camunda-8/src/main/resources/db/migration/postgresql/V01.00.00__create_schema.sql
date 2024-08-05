-- ------------------------------------------
-- Initial schema for FDA demo 
-- ------------------------------------------
create schema if not exists fdademo;
SET search_path TO fdademo;

-- Table: screen_review
CREATE TABLE screen_review
(
	review_id bigserial not null,
	shipment_id varchar(40),
	review_check varchar(60), 
	check_passed boolean, 
	automated_check boolean,
	require_manual_review boolean, 
	reviewer_required_role varchar(255) not null default '',
	check_weight int,
	create_date timestamp default current_timestamp,
	create_user varchar(40),
	constraint screen_review_pk primary key (review_id),
	constraint screen_review_uq unique (shipment_id, review_check)
)
TABLESPACE pg_default;

-- Table: shipment_entry_line
CREATE TABLE shipment_entry_line (
  shipment_id varchar(40) not null, 
  submission_date timestamp not null,
  arrival_date timestamp not null,
  port_of_entry_division varchar(40) not null,
  country_of_origin varchar(100) not null,
  product_category varchar(40) not null, 
  product_code varchar(7) not null,
  product_code_description varchar(255) not null,
  manufacturer_fei_number varchar(10),
  manufacturer_legal_name varchar(255) not null, 
  manufacturer_address1 varchar(100), 
  manufacturer_address2 varchar(100),
  manufacturer_city varchar(100), 
  manufacturer_country varchar(100), 
  filer_fei_number varchar(10),
  filer_country varchar(100),
  final_disposition varchar(60),
  final_disposition_date Date, 
  create_date timestamp default current_timestamp,
  create_user varchar(40),
  constraint shipment_entry_line_pk primary key (shipment_id)
)
TABLESPACE pg_default;

-- Table: manufacturer
create table manufacturer (
  fei_number varchar(10), 
  manufacturer_legal_name varchar(255), 
  manufacturer_address1 varchar(100), 
  manufacturer_address2 varchar(100),
  manufacturer_city varchar(100), 
  manufacturer_country varchar(100), 
  create_date timestamp default current_timestamp,
  create_user varchar(40),
  constraint manufacturer_pk primary key (fei_number)
)
TABLESPACE pg_default;

