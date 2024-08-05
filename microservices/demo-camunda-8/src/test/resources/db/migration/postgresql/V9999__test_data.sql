delete from fdademo.manufacturer;
insert into fdademo.manufacturer
(fei_number, manufacturer_legal_name,
  manufacturer_address1, manufacturer_address2,
  manufacturer_city, manufacturer_country, 
  create_date, create_user
)
values 
('0123456789', 'Human Foods Import',
'Street Address Line 1', 'Street Address Line 2',
'VeraCruz', 'Mexico', current_timestamp, 'system'
)
;
delete from fdademo.screen_review;
delete from fdademo.shipment_entry_line;
delete from fdademo.review_rule;
insert into fdademo.review_rule 
(product_category, product_code, review_check, 
automated_check, reviewer_required_role, require_manual_review,
check_weight, create_date, create_user, 
lupdate_date, lupdate_user)
values
('Human Food', '', 'priorNoticeComplianceCheck', 
true, '', false, 1, current_timestamp, 'system', 
current_timestamp, 'system'),
('Human Food', '', 'registeredManufacturerCheck', 
true, '', false, 2, current_timestamp, 'system', 
current_timestamp, 'system'),
('', '20KEO04', 'manualReviewTask', 
false, 'fda-reviewer', true, 1, current_timestamp, 'system', 
current_timestamp, 'system')
;