<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:f="http://www.samples.de/xml/fahrzeuge"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="text" />

	<xsl:template match="/">
		<xsl:apply-templates/>
	</xsl:template>

	<xsl:template match="f:fahrzeuge">
Fahrzeug-Daten-Übersicht

Hersteller:
===========
<xsl:apply-templates select="f:hersteller"/>
		
Fahrzeugtypen:
==============
<xsl:apply-templates select="f:fahrzeugtyp"/>
	</xsl:template>

<xsl:template match="f:hersteller">
<xsl:value-of select="@id"/>
------------------------------
 - Name: <xsl:value-of select="f:name"/>		
^^^^^^^^
</xsl:template>

<xsl:template match="f:fahrzeugtyp">
<xsl:variable name="hersteller"><xsl:value-of select="@hersteller" /></xsl:variable>
<xsl:value-of select="$hersteller"/><xsl:text> </xsl:text><xsl:value-of select="@name"/>
---------------------------------------
<xsl:variable name="antrieb"><xsl:value-of select="f:antrieb" /></xsl:variable>
- Antrieb: <xsl:value-of select="$antrieb"/> <xsl:choose>
<xsl:when test="$antrieb != 'Elektro'"> (Verbrenner)</xsl:when>
</xsl:choose>
- Hersteller: <xsl:value-of select="//f:hersteller[@id=$hersteller]/f:name"/>
^^^^^^^^
</xsl:template>
	
</xsl:stylesheet>
