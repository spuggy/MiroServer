<?xml version="1.0" encoding="UTF-8" ?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fo="http://www.w3.org/1999/XSL/Format">



	<xsl:output method="xml" indent="yes" />

	<!-- ============================================
		start of the first template
		=============================================== -->

	<xsl:template match="/html/body">

		<xsl:text disable-output-escaping="yes">
			&lt;!DOCTYPE fo:root [ &lt;!ENTITY tilde "&amp;#126;"&gt;
			&lt;!ENTITY florin "&amp;#131;"&gt; &lt;!ENTITY elip
			"&amp;#133;"&gt; &lt;!ENTITY dag "&amp;#134;"&gt;
			&lt;!ENTITY ddag "&amp;#135;"&gt; &lt;!ENTITY cflex
			"&amp;#136;"&gt; &lt;!ENTITY permil "&amp;#137;"&gt;
			&lt;!ENTITY uscore "&amp;#138;"&gt; &lt;!ENTITY OElig
			"&amp;#140;"&gt; &lt;!ENTITY lsquo "&amp;#145;"&gt;
			&lt;!ENTITY rsquo "&amp;#146;"&gt; &lt;!ENTITY ldquo
			"&amp;#147;"&gt; &lt;!ENTITY rdquo "&amp;#148;"&gt;
			&lt;!ENTITY bullet "&amp;#149;"&gt; &lt;!ENTITY endash
			"&amp;#150;"&gt; &lt;!ENTITY emdash "&amp;#151;"&gt;
			&lt;!ENTITY trade "&amp;#153;"&gt; &lt;!ENTITY oelig
			"&amp;#156;"&gt; &lt;!ENTITY Yuml "&amp;#159;"&gt;

			&lt;!ENTITY nbsp "&amp;#160;"&gt; &lt;!ENTITY iexcl
			"&amp;#161;"&gt; &lt;!ENTITY cent "&amp;#162;"&gt;
			&lt;!ENTITY pound "&amp;#163;"&gt; &lt;!ENTITY curren
			"&amp;#164;"&gt; &lt;!ENTITY yen "&amp;#165;"&gt;
			&lt;!ENTITY brvbar "&amp;#166;"&gt; &lt;!ENTITY sect
			"&amp;#167;"&gt; &lt;!ENTITY uml "&amp;#168;"&gt;
			&lt;!ENTITY copy "&amp;#169;"&gt; &lt;!ENTITY ordf
			"&amp;#170;"&gt; &lt;!ENTITY laquo "&amp;#171;"&gt;
			&lt;!ENTITY not "&amp;#172;"&gt; &lt;!ENTITY shy
			"&amp;#173;"&gt; &lt;!ENTITY reg "&amp;#174;"&gt;
			&lt;!ENTITY macr "&amp;#175;"&gt; &lt;!ENTITY deg
			"&amp;#176;"&gt; &lt;!ENTITY plusmn "&amp;#177;"&gt;
			&lt;!ENTITY sup2 "&amp;#178;"&gt; &lt;!ENTITY sup3
			"&amp;#179;"&gt; &lt;!ENTITY acute "&amp;#180;"&gt;
			&lt;!ENTITY micro "&amp;#181;"&gt; &lt;!ENTITY para
			"&amp;#182;"&gt; &lt;!ENTITY middot "&amp;#183;"&gt;
			&lt;!ENTITY cedil "&amp;#184;"&gt; &lt;!ENTITY sup1
			"&amp;#185;"&gt; &lt;!ENTITY ordm "&amp;#186;"&gt;
			&lt;!ENTITY raquo "&amp;#187;"&gt; &lt;!ENTITY frac14
			"&amp;#188;"&gt; &lt;!ENTITY frac12 "&amp;#189;"&gt;
			&lt;!ENTITY frac34 "&amp;#190;"&gt; &lt;!ENTITY iquest
			"&amp;#191;"&gt; &lt;!ENTITY Agrave "&amp;#192;"&gt;
			&lt;!ENTITY Aacute "&amp;#193;"&gt; &lt;!ENTITY Acirc
			"&amp;#194;"&gt; &lt;!ENTITY Atilde "&amp;#195;"&gt;
			&lt;!ENTITY Auml "&amp;#196;"&gt; &lt;!ENTITY Aring
			"&amp;#197;"&gt; &lt;!ENTITY AElig "&amp;#198;"&gt;
			&lt;!ENTITY Ccedil "&amp;#199;"&gt; &lt;!ENTITY Egrave
			"&amp;#200;"&gt; &lt;!ENTITY Eacute "&amp;#201;"&gt;
			&lt;!ENTITY Ecirc "&amp;#202;"&gt; &lt;!ENTITY Euml
			"&amp;#203;"&gt; &lt;!ENTITY Igrave "&amp;#204;"&gt;
			&lt;!ENTITY Iacute "&amp;#205;"&gt; &lt;!ENTITY Icirc
			"&amp;#206;"&gt; &lt;!ENTITY Iuml "&amp;#207;"&gt;
			&lt;!ENTITY ETH "&amp;#208;"&gt; &lt;!ENTITY Ntilde
			"&amp;#209;"&gt; &lt;!ENTITY Ograve "&amp;#210;"&gt;
			&lt;!ENTITY Oacute "&amp;#211;"&gt; &lt;!ENTITY Ocirc
			"&amp;#212;"&gt; &lt;!ENTITY Otilde "&amp;#213;"&gt;
			&lt;!ENTITY Ouml "&amp;#214;"&gt; &lt;!ENTITY times
			"&amp;#215;"&gt; &lt;!ENTITY Oslash "&amp;#216;"&gt;
			&lt;!ENTITY Ugrave "&amp;#217;"&gt; &lt;!ENTITY Uacute
			"&amp;#218;"&gt; &lt;!ENTITY Ucirc "&amp;#219;"&gt;
			&lt;!ENTITY Uuml "&amp;#220;"&gt; &lt;!ENTITY Yacute
			"&amp;#221;"&gt; &lt;!ENTITY THORN "&amp;#222;"&gt;
			&lt;!ENTITY szlig "&amp;#223;"&gt; &lt;!ENTITY agrave
			"&amp;#224;"&gt; &lt;!ENTITY aacute "&amp;#225;"&gt;
			&lt;!ENTITY acirc "&amp;#226;"&gt; &lt;!ENTITY atilde
			"&amp;#227;"&gt; &lt;!ENTITY auml "&amp;#228;"&gt;
			&lt;!ENTITY aring "&amp;#229;"&gt; &lt;!ENTITY aelig
			"&amp;#230;"&gt; &lt;!ENTITY ccedil "&amp;#231;"&gt;
			&lt;!ENTITY egrave "&amp;#232;"&gt; &lt;!ENTITY eacute
			"&amp;#233;"&gt; &lt;!ENTITY ecirc "&amp;#234;"&gt;
			&lt;!ENTITY euml "&amp;#235;"&gt; &lt;!ENTITY igrave
			"&amp;#236;"&gt; &lt;!ENTITY iacute "&amp;#237;"&gt;
			&lt;!ENTITY icirc "&amp;#238;"&gt; &lt;!ENTITY iuml
			"&amp;#239;"&gt; &lt;!ENTITY eth "&amp;#240;"&gt;
			&lt;!ENTITY ntilde "&amp;#241;"&gt; &lt;!ENTITY ograve
			"&amp;#242;"&gt; &lt;!ENTITY oacute "&amp;#243;"&gt;
			&lt;!ENTITY ocirc "&amp;#244;"&gt; &lt;!ENTITY otilde
			"&amp;#245;"&gt; &lt;!ENTITY ouml "&amp;#246;"&gt;
			&lt;!ENTITY oslash "&amp;#248;"&gt; &lt;!ENTITY ugrave
			"&amp;#249;"&gt; &lt;!ENTITY uacute "&amp;#250;"&gt;
			&lt;!ENTITY ucirc "&amp;#251;"&gt; &lt;!ENTITY uuml
			"&amp;#252;"&gt; &lt;!ENTITY yacute "&amp;#253;"&gt;
			&lt;!ENTITY thorn "&amp;#254;"&gt; &lt;!ENTITY yuml
			"&amp;#255;"&gt; ]&gt;
		</xsl:text>


		<fo:root>
			<fo:layout-master-set>
				<fo:simple-page-master master-name="noheader" page-height="29.7cm" page-width="21.0cm" margin-left="2.0cm" margin-right="0.0cm">
					<fo:region-body margin="0cm" />
				</fo:simple-page-master>
				
				<fo:simple-page-master master-name="A4" page-height="29.7cm" page-width="21.0cm" margin-left="2cm" margin-right="2cm" margin-top="0.5cm"   margin-bottom="0.1cm">
					<fo:region-body margin-top="2cm" margin-bottom="2cm"/>
					<fo:region-before extent="2cm" />
					<fo:region-after  extent="2cm" />
				
				</fo:simple-page-master>
			</fo:layout-master-set>

			<xsl:apply-templates />
		</fo:root>



	</xsl:template>

	<xsl:template match="/html/body/div[@id = '0' ]">
		<fo:page-sequence master-reference="noheader"  force-page-count="no-force">
			<fo:flow flow-name="xsl-region-body">
				<fo:block >
					<xsl:apply-templates />
				</fo:block>
			</fo:flow>
		</fo:page-sequence>


	</xsl:template>


	<xsl:template match="/html/body/div[@id &gt; '0' ]">
		<fo:page-sequence  master-reference="A4"  >

			<fo:static-content flow-name="xsl-region-before">
								<fo:block border-bottom-width="0.25mm" border-bottom-style="solid">
						<fo:external-graphic src="url('/miro-reports/images/miro-logo.jpg')" content-height="35px"  content-width="75px"/>
				</fo:block>
			</fo:static-content>
			<fo:static-content flow-name="xsl-region-after" >
				<fo:block text-align="center" font-size="11pt" line-height="13pt" space-after="11pt" font-family="Arial" border-top-width="0.25mm" border-top-style="solid" >
					<fo:page-number />
					<fo:block font-size="10pt" line-height="13pt" >
			&#169; MiRo Psychometrics Ltd 2008
		</fo:block>
					
				</fo:block>
			</fo:static-content>
			<fo:flow flow-name="xsl-region-body">
				<fo:block>
					<xsl:apply-templates />
				</fo:block>
			</fo:flow>
		</fo:page-sequence>


	</xsl:template>





	<xsl:template match="p">
		<fo:block text-align="justify" font-size="11pt" line-height="13pt" space-after="11pt">
			<xsl:apply-templates select="*|text()" />
		</fo:block>
	</xsl:template>
	
	<xsl:template match="pp">
		<fo:block text-align="justify" font-size="11pt" line-height="13pt" >
			<xsl:apply-templates select="*|text()" />
		</fo:block>
	</xsl:template>


	<xsl:template match="ul">
		<fo:list-block provisional-distance-between-starts="1cm"
			provisional-label-separation="0.5cm">
			<xsl:attribute name="space-after">
				<xsl:choose>
					<xsl:when test="ancestor::ul or ancestor::ol">
						<xsl:text>0pt</xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:text>11pt</xsl:text>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<xsl:attribute name="start-indent">
				<xsl:variable name="ancestors">
					<xsl:choose>
						<xsl:when
							test="count(ancestor::ol) or count(ancestor::ul)">
							<xsl:value-of
								select="1 + 
                                    (count(ancestor::ol) + 
                                     count(ancestor::ul)) * 
                                    1.25" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>1</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<xsl:value-of select="concat($ancestors, 'cm')" />
			</xsl:attribute>
			<xsl:apply-templates select="*" />
		</fo:list-block>
	</xsl:template>

	<!-- ============================================
		List items inside unordered lists are easy; we
		just have to use the correct Unicode character
		for the bullet.  
		=============================================== -->

	<xsl:template match="ul/li">
		<fo:list-item>
			<fo:list-item-label end-indent="label-end()">
				<fo:block>&#x2022;</fo:block>
			</fo:list-item-label>
			<fo:list-item-body start-indent="body-start()">
				<fo:block font-size="11pt" font-family="Arial" line-height="13pt" space-after="11pt">
					<xsl:apply-templates select="*|text()" />
				</fo:block>
			</fo:list-item-body>
		</fo:list-item>
	</xsl:template>


	<xsl:template match="h1">

		<fo:block font-size="28pt" line-height="32pt" font-weight="bold"
			keep-with-next="always" space-after="22pt" font-family="Arial">
			<xsl:attribute name="id">
				<xsl:choose>
					<xsl:when test="@id">
						<xsl:value-of select="@id" />
					</xsl:when>
					<xsl:when
						test="name(preceding-sibling::*[1]) = 'a' and
                          preceding-sibling::*[1][@name]">
						<xsl:value-of
							select="preceding-sibling::*[1]/@name" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="generate-id()" />
					</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<xsl:apply-templates select="*|text()" />
		</fo:block>
	</xsl:template>

	<!-- ============================================
		<h2> is in a slightly smaller font than an <h1>,
		and it doesn't have a page break or a line.
		=============================================== -->

	<xsl:template match="h2">
		<fo:block font-size="24pt" line-height="28pt" font-weight="bold"
			keep-with-next="always" space-after="18pt" font-family="Arial">
			<xsl:attribute name="id">
				<xsl:choose>
					<xsl:when test="@id">
						<xsl:value-of select="@id" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="generate-id()" />
					</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<xsl:apply-templates select="*|text()" />
		</fo:block>
	</xsl:template>

	<!-- ============================================
		<h3> is slightly smaller than <h2>.
		=============================================== -->

	<xsl:template match="h3">
		<fo:block font-size="14pt" line-height="18pt" font-weight="bold"
			keep-with-next="always" space-after="7pt" font-family="Arial">
			<xsl:attribute name="id">
				<xsl:choose>
					<xsl:when test="@id">
						<xsl:value-of select="@id" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="generate-id()" />
					</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<xsl:apply-templates select="*|text()" />
		</fo:block>
	</xsl:template>

	<!-- ============================================
		<h4> is smaller than <h3>.  For the bookmarks
		and table of contents, <h4> is the lowest level
		we include.
		=============================================== -->

	<xsl:template match="h4">
		<fo:block font-size="12pt" line-height="14pt" font-weight="bold"
			keep-with-next="always" space-after="6pt" font-family="Arial">
			<xsl:attribute name="id">
				<xsl:choose>
					<xsl:when test="@id">
						<xsl:value-of select="@id" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="generate-id()" />
					</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<xsl:apply-templates select="*|text()" />
		</fo:block>
	</xsl:template>

	<!-- ============================================
		<h5> is pretty small, and is underlined to 
		help it stand out. 
		=============================================== -->

	<xsl:template match="h5">
		<fo:block font-size="12pt" line-height="19pt"
			keep-with-next="always" space-after="12pt" font-family="Arial"
			text-decoration="underline">
			<xsl:attribute name="id">
				<xsl:choose>
					<xsl:when test="@id">
						<xsl:value-of select="@id" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="generate-id()" />
					</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<xsl:apply-templates select="*|text()" />
		</fo:block>
	</xsl:template>

	<!-- ============================================
		<h6> is the smallest heading of all, and is
		underlined and italicized.  
		=============================================== -->

	<xsl:template match="h6">
		<fo:block font-size="11pt" line-height="17pt"
			keep-with-next="always" space-after="12pt" font-family="Arial"
			font-style="italic" text-decoration="underline">
			<xsl:attribute name="id">
				<xsl:choose>
					<xsl:when test="@id">
						<xsl:value-of select="@id" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="generate-id()" />
					</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<xsl:apply-templates select="*|text()" />
		</fo:block>
	</xsl:template>

	<!-- ============================================
		We render an <hr> with a leader.  Because <hr>
		is empty, we don't have to process any child
		elements. 
		=============================================== -->

	<xsl:template match="hr">
		<fo:block>
			<fo:leader leader-pattern="rule" />
		</fo:block>
	</xsl:template>

	<xsl:template match="b">
		<fo:inline font-weight="bold">
			<xsl:apply-templates select="*|text()" />
		</fo:inline>
	</xsl:template>

	<xsl:template match="mironame">
		<fo:inline font-weight="bold">
			<xsl:apply-templates select="*|text()" />
		</fo:inline>
	</xsl:template>


	<xsl:template match="img">
		<fo:block space-after="12pt">
			<fo:external-graphic src="{@src}" scaling="uniform">
				<xsl:if test="@width">
					<xsl:attribute name="content-width">
						<xsl:choose>
							<xsl:when test="contains(@width, 'px')">
								<xsl:value-of select="@width" />
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of
									select="concat(@width, 'px')" />
							</xsl:otherwise>
						</xsl:choose>
					</xsl:attribute>
				</xsl:if>
				<xsl:if test="@height">
					<xsl:attribute name="content-height">
						<xsl:choose>
							<xsl:when test="contains(@height, 'px')">
								<xsl:value-of select="@height" />
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of
									select="concat(@height, 'px')" />
							</xsl:otherwise>
						</xsl:choose>
					</xsl:attribute>
				</xsl:if>
			</fo:external-graphic>
		</fo:block>
	</xsl:template>



</xsl:stylesheet>