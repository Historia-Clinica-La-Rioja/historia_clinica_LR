drop schema if exists snomedct cascade;
create schema snomedct;

set schema 'snomedct';

/*create table concept*/
CREATE TABLE concept(
  id varchar(18) not null,
  effectivetime char(8) not null,
  active char(1) not null,
  moduleid varchar(18) not null,
  definitionstatusid varchar(18) not null,
  CONSTRAINT PK_concept PRIMARY KEY(id, effectivetime)
);

/*create table description*/
CREATE TABLE description(
  id varchar(18) not null,
  effectivetime char(8) not null,
  active char(1) not null,
  moduleid varchar(18) not null,
  conceptid varchar(18) not null,
  languagecode varchar(2) not null,
  typeid varchar(18) not null,
  term text not null,
  casesignificanceid varchar(18) not null,
  CONSTRAINT PK_description PRIMARY KEY(id, effectivetime)
);

/*create table relationship */
CREATE TABLE relationship(
  id varchar(18) not null,
  effectivetime char(8) not null,
  active char(1) not null,
  moduleid varchar(18) not null,
  sourceid varchar(18) not null,
  destinationid varchar(18) not null,
  relationshipgroup varchar(18) not null,
  typeid varchar(18) not null,
  characteristictypeid varchar(18) not null,
  modifierid varchar(18) not null,
  CONSTRAINT PK_relationship PRIMARY KEY(id, effectivetime)
);

/*create table langrefset*/
CREATE TABLE langrefset(
  id uuid not null,
  effectivetime char(8) not null,
  active char(1) not null,
  moduleid varchar(18) not null,
  refsetid varchar(18) not null,
  referencedcomponentid varchar(18) not null,
  acceptabilityid varchar(18) not null,
  CONSTRAINT PK_langrefset PRIMARY KEY(id, effectivetime)
);

/*create table relationship_concrete_value */
CREATE TABLE relationship_concrete_value(
  id varchar(18) not null,
  effectivetime char(8) not null,
  active char(1) not null,
  moduleid varchar(18) not null,
  sourceId varchar(18) not null,
  value varchar(18) not null,
  relationshipgroup varchar(18) not null,
  typeid varchar(18) not null,
  characteristictypeid varchar(18) not null,
  modifierid varchar(18) not null,
  CONSTRAINT PK_relationship_concrete_value PRIMARY KEY(id, effectivetime)
);

/*create table concept_term */
CREATE TABLE concept_term(
  id varchar(18) not null,
  term text not null,
  def_status varchar(18) not null,
  type varchar(5) not null,
  acceptability varchar(18) not null
);


CREATE INDEX IF NOT EXISTS "IDX_description_conceptid" ON snomedct.description (conceptid);

CREATE INDEX IF NOT EXISTS "IDX_langrefset_referencedcomponentid" ON snomedct.langrefset (referencedcomponentid);

CREATE INDEX IF NOT EXISTS "IDX_relationship_destinationid" ON snomedct.relationship (destinationid);
CREATE INDEX IF NOT EXISTS "IDX_relationship_sourceid" ON snomedct.relationship (sourceid);
CREATE INDEX IF NOT EXISTS "IDX_relationship_typeid" ON snomedct.relationship (typeid);

CREATE INDEX IF NOT EXISTS "IDX_relationship_concrete_value_sourceid" ON snomedct.relationship_concrete_value (sourceid);
CREATE INDEX IF NOT EXISTS "IDX_relationship_concrete_value_typeid" ON snomedct.relationship_concrete_value (typeid);

CREATE INDEX IF NOT EXISTS "IDX_concept_term_id" ON snomedct.concept_term (id);