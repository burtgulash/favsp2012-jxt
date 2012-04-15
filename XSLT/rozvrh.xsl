<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="2.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:fn="http://www.w3.org/2005/xpath-functions">
<xsl:output method="xml" encoding="utf-8" />
	
<xsl:variable name="this" select="/" />
<xsl:variable name="data" select="document('data.xml')" />

<xsl:template name="zaSemestr">
	<xsl:param name="semestr" />
	<semestr zimniLetni="{$semestr}">
		<xsl:for-each select="$data/data/dny/den">
			<xsl:variable name="den" select="." />
			<radka>
				<den><xsl:value-of select="." /></den>
				<xsl:for-each select="$this/student/predmet[semestr = $semestr
													and den = $den]">
					<xsl:sort select="zacatek" />
					<xsl:copy-of select="." />
				</xsl:for-each>
			</radka>
		</xsl:for-each>
	</semestr>
</xsl:template>


<xsl:template match="/" mode="faze1">
	<rozvrh>
		<xsl:call-template name="zaSemestr">
			<xsl:with-param name="semestr" select="'ZS'" />
		</xsl:call-template>
		<xsl:call-template name="zaSemestr">
			<xsl:with-param name="semestr" select="'LS'" />
		</xsl:call-template>
	</rozvrh>
</xsl:template>


<xsl:template match="*|/" mode="faze2">
	<xsl:copy>
		<xsl:apply-templates mode="faze2" />
	</xsl:copy>
</xsl:template>

<xsl:template match="radka" mode="faze2">
	<xsl:variable name="radka" select="." />

	<!-- Vypočítat maximum kolizí pro daný den. -->
	<xsl:variable name="maxKolizi">
		<xsl:for-each select="$data/data/casy/hodina">
				<xsl:sort data-type="number" order="descending"
	select="count($radka/predmet[xs:time(zacatek) &lt;= xs:time(current()/zacatek)
						and xs:time(konec) &gt;= xs:time(current()/konec)])" />
			<xsl:if test="position() = 1">
				<xsl:value-of
	select="count($radka/predmet[xs:time(zacatek) &lt;= xs:time(current()/zacatek)
					and xs:time(konec) &gt;= xs:time(current()/konec)])" />

			</xsl:if>
		</xsl:for-each>
	</xsl:variable>

	<faze2den>
		<rowspan><xsl:value-of select="$maxKolizi" /></rowspan>
		<xsl:variable name="predmety" select="predmet" />
		<xsl:apply-templates select="den" mode="faze2" />
		<xsl:for-each select="0 to xs:integer($maxKolizi) - 1">
			<xsl:variable name="i" select="." />
			<faze2radka>
				<xsl:for-each select=
				"$predmety[
					count(
						$predmety[xs:time(zacatek) &lt;= xs:time(../zacatek)
						   	  and xs:time(konec) &gt;= xs:time(../zacatek)]
						 ) = $i]">
					<xsl:value-of select="nazev" />
				</xsl:for-each>
			</faze2radka>
		</xsl:for-each>
	</faze2den>
</xsl:template>
	



<xsl:variable name="vysledekFaze1">
	<xsl:apply-templates select="/" mode="faze1" />
</xsl:variable>

<xsl:variable name="vysledekFaze2">
	<xsl:apply-templates select="$vysledekFaze1" mode="faze2" />
</xsl:variable>

<xsl:template match="/">
	<xsl:apply-templates select="$vysledekFaze2" mode="faze2" />
</xsl:template>
</xsl:stylesheet>
